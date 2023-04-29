package com.SmartTech.teasyNew.activity.base;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.SmartTech.teasyNew.AppConfig;
import com.SmartTech.teasyNew.Session;
import com.SmartTech.teasyNew.activity.ActivityLogin;
import com.SmartTech.teasyNew.activity.annotation.Lockable;
import com.SmartTech.teasyNew.activity.annotation.ReturnToActivity;
import com.SmartTech.teasyNew.api_new.appmanager.AppMananagerAPI;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetBalanceV2Response;
import com.SmartTech.teasyNew.model.ContactModel;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by muddvayne on 21/06/2017.
 */

public class BaseActivity extends AppCompatActivity {

    protected Session session;

    protected AppMananagerAPI appMananagerAPI;

    private boolean isLockable;

    private long lastActivityTimestamp = 0;

    private long appPausedTimestamp = 0;

    private  InactivityTimeoutChecker inactivityChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = (Session) getIntent().getSerializableExtra("Session");

        //check if activity is lockable
        Annotation annotation = getClass().getAnnotation(Lockable.class);
        isLockable = (annotation != null);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConfig.getProperties().getProperty("appmanager_api.url"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        appMananagerAPI = retrofit.create(AppMananagerAPI.class);
    }

    public AppMananagerAPI getAppMananagerAPI() {
        return appMananagerAPI;
    }

    public boolean isLockable() {
        return isLockable;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public void onUserInteraction(){
        if(isLockable()) {
            lastActivityTimestamp = System.currentTimeMillis();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(isLockable()) {
            appPausedTimestamp = System.currentTimeMillis();
            inactivityChecker.abort();
        }

        if(session != null && session.getDeviceInfo() != null) {
            session.getDeviceInfo().stopLocationUpdate();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(isLockable()) {
            appPausedTimestamp = System.currentTimeMillis();
            inactivityChecker.abort();
        }

        if(session != null && session.getDeviceInfo() != null) {
            session.getDeviceInfo().stopLocationUpdate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<ContactModel> mContacts = new ArrayList<>();
        if(session !=null && session.getContacts() !=null) {
            mContacts = session.getContacts();
        }

        if(session != null && session.getDeviceInfo() != null) {
            session.getDeviceInfo().startLocationUpdate(this);
        }

        if(isLockable()) {
            if (appPausedTimestamp > 0 && System.currentTimeMillis() - appPausedTimestamp > AppConfig.DELAY_BEFORE_PAUSED_APP_LOCKS) {
                Intent intent = new Intent(this, ActivityLogin.class);
                intent.putExtra("Smart_Contacts", mContacts);
                startActivity(intent);
                finish();
                return;
            }

            lastActivityTimestamp = System.currentTimeMillis();

            inactivityChecker = new InactivityTimeoutChecker();
            Thread t = new Thread(inactivityChecker);
            t.start();
        }
    }

    private class InactivityTimeoutChecker implements Runnable {

        private boolean aborted = false;

        @Override
        public void run() {
            try {
                while(true) {
                    if(aborted) {
                        break;
                    }

                    Thread.sleep(1000);

                    if (System.currentTimeMillis() - lastActivityTimestamp > AppConfig.INACTIVITY_TIMEOUT) {
                        Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                        intent.putExtra("session_expired", true);
                        intent.putExtra("Smart_Contacts", session.getContacts());
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            } catch (InterruptedException e) {}
        }

        public void abort() {
            aborted = true;
        }
    }

    public void updateBalance() {
        getAppMananagerAPI().getBalanceV2Request(
                session.walletId,
                session.pin,
                session.getAccountType().name(),
                session.getAgentShortCode()
        ).enqueue(new Callback<GetBalanceV2Response>() {
            @Override
            public void onResponse(Call<GetBalanceV2Response> call, Response<GetBalanceV2Response> response) {
                if(response.isSuccessful()) {
                    GetBalanceV2Response responseBody = response.body();
                    if(responseBody != null && responseBody.getResponseCode() == BaseResponse.ResponseCode.OK) {
                        session.setMainBalance(responseBody.mainBalance);
                        session.setCommissionBalance(responseBody.commissionBalance);

                        onBalanceUpdate();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBalanceV2Response> call, Throwable t) {

            }
        });
    }

    //you might want to override this method in subclasses
    protected void onBalanceUpdate() {
        return;
    }

    @Override
    public boolean onSupportNavigateUp() {
        ReturnToActivity annotation = getClass().getAnnotation(ReturnToActivity.class);
        if(annotation != null) {
            returnToActivity(
                    annotation.activity(), annotation.passExtras()
            );
            return true;
        }
        else {
            return super.onSupportNavigateUp();
        }
    }

    @Override
    public void onBackPressed() {
        ReturnToActivity annotation = getClass().getAnnotation(ReturnToActivity.class);
        if(annotation != null) {
            returnToActivity(
                    annotation.activity(), annotation.passExtras()
            );
        }
        else {
            super.onBackPressed();
        }
    }

    private void returnToActivity(Class<? extends BaseActivity> activity, boolean passExtras) {
        Intent intent = new Intent(this, activity);

        if(passExtras) {
            Bundle extras = getIntent().getExtras();
            intent.putExtras(extras);
        }

        intent.putExtra("Session", session);
        startActivity(intent);
        finish();
    }


}
