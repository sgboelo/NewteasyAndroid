package com.SmartTech.teasyNew.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.SmartTech.teasyNew.AppConfig;
import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.api_new.appmanager.AppMananagerAPI;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AIRSTaxListResponse;
import com.SmartTech.teasyNew.view.privacy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by muddvayne on 14/08/2017.
 */

public class ActivitySplash extends AppCompatActivity {

    private static final List<String> PERMISSIONS_MANDATORY = new ArrayList<>();
    private static final List<String> PERMISSIONS_OPTIONAL = new ArrayList<>();
    private static final int REQUEST_PERMISSIONS = 1;
    private static final int SPLASH_MIN_DELAY = 3000;
    private SharedPreferences prefs;
    private long startTime;
    private int asyncTasksCompleted = 0;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        //connectivityCheck mConnectionCheck = new connectivityCheck(this);
        PERMISSIONS_MANDATORY.add(Manifest.permission.READ_CONTACTS);
        PERMISSIONS_MANDATORY.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            PERMISSIONS_MANDATORY.add(Manifest.permission.READ_PHONE_STATE);
        }
        else {
            PERMISSIONS_MANDATORY.add(Manifest.permission.READ_PHONE_NUMBERS);

        }

        //initialization of props could take some time if app is installed on external storage
        //so that it's better to init them here
        AppConfig.getProperties();
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        startTime = System.currentTimeMillis();
        loading();

    }

    private void loadAIRSTaxList() {
        final String KEY_AIRS_TAX_LIST = "KEY_AIRS_TAX_LIST";
        final String KEY_AIRS_TAX_LIST_LAST_UPDATED = "KEY_AIRS_TAX_LIST_LAST_UPDATED";

        boolean needUpdate = false;
        String cachedList = prefs.getString(KEY_AIRS_TAX_LIST, null);
        if(cachedList == null) {
            needUpdate = true;
        }

        long lastUpdated = prefs.getLong(KEY_AIRS_TAX_LIST_LAST_UPDATED, -1);
        if(lastUpdated < 0) {
            needUpdate = true;
        }
        else {
            long diff = System.currentTimeMillis() - lastUpdated;
            if(diff > 3600 * 1000) {    //an hour has passed
                needUpdate = true;
            }

            if(diff < 0) {  //something went wrong
                needUpdate = true;
            }
        }

        if(needUpdate) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(AppConfig.getProperties().getProperty("appmanager_api.url"))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AppMananagerAPI appMananagerAPI = retrofit.create(AppMananagerAPI.class);
            appMananagerAPI.airsTaxListRequest("default,quickpay").enqueue(new Callback<AIRSTaxListResponse>() {
                @Override
                public void onResponse(Call<AIRSTaxListResponse> call, Response<AIRSTaxListResponse> response) {

                    if(response.code() != 200) {
                        asyncTaskCompleted();
                        return;
                    }

                    AIRSTaxListResponse responseBody = response.body();
                    if(responseBody == null || responseBody.status != 0) {
                        asyncTaskCompleted();
                        return;
                    }

                    String json = new Gson().toJson(responseBody);
                    prefs.edit().putString(KEY_AIRS_TAX_LIST, json).apply();
                    prefs.edit().putLong(KEY_AIRS_TAX_LIST_LAST_UPDATED, System.currentTimeMillis()).apply();
                    asyncTaskCompleted();
                }

                @Override
                public void onFailure(Call<AIRSTaxListResponse> call, Throwable t) {
                    asyncTaskCompleted();
                }
            });
        }
        else {
            asyncTaskCompleted();
        }
    }

    private synchronized void asyncTaskCompleted() {
        if(++asyncTasksCompleted < 1) {
            return;
        }

        long endTime = System.currentTimeMillis();
        long delay = Math.max(1, SPLASH_MIN_DELAY - (endTime - startTime));

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                List<String> permissions = new ArrayList<>(PERMISSIONS_MANDATORY);
                permissions.addAll(PERMISSIONS_OPTIONAL);

                List<String> permissionRequests = new ArrayList<>();

                for(String permission : permissions) {
                    if(!isPermissionMandatory(permission)) {
                        //optional permissions will be asked just once

                        boolean alreadyAsked = prefs.getBoolean(
                                "PERMISSION_" + permission + "_ASKED",
                                false
                        );

                        if(alreadyAsked) {
                            continue;
                        }
                    }

                    int permissionCheck = ContextCompat.checkSelfPermission(ActivitySplash.this, permission);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        permissionRequests.add(permission);
                    }
                }


                if(permissionRequests.size() > 0) {
                    ActivityCompat.requestPermissions(
                            ActivitySplash.this,
                            permissionRequests.toArray(new String[permissionRequests.size()]),
                            REQUEST_PERMISSIONS
                    );
                }
                else {
                    privacy();
                }
            }
        }, delay);
    }

    private void nextActivity() {
        Intent intent ;
        intent = new Intent(ActivitySplash.this, ActivityLogin.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            SharedPreferences.Editor editor = prefs.edit();

            boolean mandatoryPermissionDenied = false;
            for (int i = 0; i < permissions.length; ++i) {
                String permission = permissions[i];

                if (!isPermissionMandatory(permission)) {
                    //optional permissions will be asked just once
                    editor.putBoolean("PERMISSION_" + permission + "_ASKED", true);
                }

                //make sure that all mandatory permissions have been given
                if (isPermissionMandatory(permission)
                        && grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                    mandatoryPermissionDenied = true;
                }
            }

            editor.apply();

            if (mandatoryPermissionDenied) {
                finish();
            } else {
                privacy();

            }
        }
    }

    private boolean isPermissionMandatory(String permission) {
        for(String value : PERMISSIONS_MANDATORY) {
            if(value.equals(permission)) {
                return true;
            }
        }


        return false;
    }


    //Check internet connection and reload if there is non
    private void loading(){
        ConnectivityManager conMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();




        if (netInfo == null){


            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("No Please make sure your phone has internet and try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            loading();
                        }
                    }).show();

        }else{
            loadAIRSTaxList();
            if(new database(this).getNotificationToggleStatus() == 3){
                new database(this).insertToggle(2,0);
            }
        }

    }


    private void privacy(){
        if(new database(ActivitySplash.this).getPolicyStatus() != 1 ){
        privacy privacyPolicy = new privacy(ActivitySplash.this);
        privacyPolicy.privacyPolicy();
        privacyPolicy.getCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privacyPolicy.getMyDialog().dismiss();
                finish();
            }
        });

        privacyPolicy.getAccept().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new database(ActivitySplash.this).insertToggle(3,1);
                nextActivity();
            }
        });

        privacyPolicy.getMyDialog().show();


        }else{
            nextActivity();
        }
    }


}
