package com.SmartTech.teasyNew;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.SmartTech.teasyNew.activity.ActivitySplash;
import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.database;
import com.SmartTech.teasyNew.api_new.appmanager.AppMananagerAPI;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetNotificationsResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.TransactionHistoryResponse;
import com.SmartTech.teasyNew.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationService extends Service{
    protected AppMananagerAPI appMananagerAPI;
    private String CHANNEL_ID = "CHANNEL_TEASY";
    public static boolean started = false;
    String pin, wallet, shortcode,type;
    @Override
    public void onCreate() {
        String dPin = Encrytion.decrypt(new database(getApplicationContext()).getTPin());
        String dWallet = Encrytion.decrypt(new database(getApplicationContext()).getAllWallet().getWallet());
        String dshortcode = Encrytion.decrypt(new database(getApplicationContext()).getAllWallet().getShortcode());
        String dType = Encrytion.decrypt(new database(getApplicationContext()).getAllWallet().getType());
        pin = dPin;
        wallet = dWallet;
        shortcode = dshortcode;
        type = dType;
        startService("");
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        //HandlerThread thread = new HandlerThread("ServiceStartArguments",
                //Process.THREAD_PRIORITY_BACKGROUND);
        //thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        //serviceLooper = thread.getLooper();
        //serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override

    // execution of service will start
    // on calling this method
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job

        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(true)
                {
                    try {
                        Thread.sleep(5000);
                        Notification_Pop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //REST OF CODE HERE//
                }

            }
        }).start();

        // If we get killed, after returning from here, restart
        started = true;
        return START_STICKY;
    }

    @Override

    // execution of the service will
    // stop on calling this method
    public void onDestroy() {

        // call MyReceiver which will restart this service via a worker
        started = false;
        super.onDestroy();



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    public AppMananagerAPI getAppMananagerAPI() {
        return appMananagerAPI;
    }
    private NotificationCompat.Builder Notification(String Title,String Body,String LargeBody){
        String lBody = "";
        if(!LargeBody.equals("")){
            lBody = LargeBody;
        }
        Intent intent = new Intent(this, ActivitySplash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_3color)
                .setContentTitle(Title)
                .setContentText(Body)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(lBody))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder;
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TeasyPay";
            String description = "Teasy Notification Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void Notification_Pop(){
        List<Notification> notification = new ArrayList<>();
        Callback<GetNotificationsResponse> callback = new Callback<GetNotificationsResponse>() {
            @Override
            public void onResponse(Call<GetNotificationsResponse> call, Response<GetNotificationsResponse> response) {

                if(!response.isSuccessful()) {
                    onFail();
                    return;
                }
                GetNotificationsResponse responseBody = response.body();

                if(responseBody.getNotificationList() == null){
                    return;
                }

                if(new database(getApplicationContext()).getNotificationSize() <1) {
                    new database(getApplicationContext()).insertNotification
                            (1,responseBody.getNotificationList().get(0).created.toString());

                }
                notification.addAll(responseBody.getNotificationList());

                if (!(notification.size() >0)){
                    return;
                }

                if(!new database(getApplicationContext()).getAllNotification().equals(notification.get(0).created.toString())){

                    for(int i = 0; i < notification.size(); i++){
                        if(new database(getApplicationContext()).getAllNotification().equals(notification.get(i).created.toString())){
                            break;
                        }
                        createNotificationChannel();

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        String mAmount = "₦" + Utils.formatBalance(notification.get(i).amount);
                        int direction = notification.get(i).direction;
                        String rDirection;
                        String temp = "";
                        String transactionType =  String.valueOf(notification.get(i).getType());

                        if(direction == 1){
                            rDirection = "Credited";
                        }else{
                            rDirection = "Debited";
                        }
                        if(transactionType.equals("AIRTIME_PURCHASE")){
                            temp = "Type: AIRTIME PURCHASE\n"
                                    +"Receiver: ";
                        }else if(transactionType.equals("WALLET_TRANSFER")){
                            if(direction ==1) {
                                temp = "Type: WALLET TRANSFER\n"
                                        + "Sender Wallet: "+walletToWallet(notification.get(i).remarks);
                            }else{
                                temp = "Type: WALLET TRANSFER\n"
                                        + "Receiver Wallet: "+walletToWallet(notification.get(i).remarks);
                            }
                        }else if(transactionType.equals("BANK_TRANSFER")){
                            if(notification.get(i).getStatus().toString().equals("SUCCESS")) {
                                temp = "Type: BANK TRANSFER\n"
                                        + "Bank:" + bankRemark(notification.get(i).remarks)[1] + "\n"
                                        + "Account Name: " + bankRemark(notification.get(i).remarks)[0] + "\n"
                                        + "Receiver AC: " + bankRemark(notification.get(i).remarks)[2] + "\n"
                                        + "Amount: " + mAmount;
                            }
                        }

                        String finalShortString = "Your Account have been " + rDirection + " With " + mAmount;
                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(i,
                                Notification("TeasyPay Alert",
                                        finalShortString,
                                        temp).build());

                    }
                    new database(getApplicationContext())
                            .insertNotification
                                    (1,responseBody
                                            .getNotificationList()
                                            .get(0).created
                                            .toString());
                    notification.clear();
                }

            }

            @Override
            public void onFailure(Call<GetNotificationsResponse> call, Throwable t) {

                onFail();
            }

            private void onFail() {
            }
        };

        getAppMananagerAPI().getNotificationsRequest(
                wallet,
                pin,
                type,
                shortcode
        ).enqueue(callback);

    }

    private String [] bankRemark(String Remark){
        String[] mArray = new String[3];
        int position1, position2 , position3;
        position1 = Remark.indexOf(":");
        position2 = Remark.indexOf(":",position1+1);
        position3 = Remark.lastIndexOf(":");
        mArray[0] = Remark.substring(position1+1, position2-("Financial Institution".length()+1));
        mArray[1] = Remark.substring(position2+1, position3-("Account".length()+1));
        mArray[2] = Remark.substring(position3+1);

        return mArray;
    }
    private String  walletToWallet(String Remark){
        return Remark.substring(Remark.indexOf(":")+1);
    }

    public void startService(String Smart) {
        if (!NotificationService.started) {
            Intent serviceIntent = new Intent(this, NotificationService.class);
            startService(serviceIntent);
        }
    }
    private void transaction(MainActivity teasyMain){

        try {
            Callback<TransactionHistoryResponse> callback = new Callback<TransactionHistoryResponse>() {
                @Override
                public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {


                    TransactionHistoryResponse responseBody = response.body();
                    if (!response.isSuccessful() || responseBody == null) {
                        onFail();
                        return;
                    }

                    if (responseBody.getResponseCode() != BaseResponse.ResponseCode.OK) {
                        onFail();
                        return;
                    }

                    if (responseBody.transactions.size() > 0) {

                    }
                }

                @Override
                public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {

                    onFail();
                }

                private void onFail() {


                }
            };

            teasyMain.getAppMananagerAPI().transactionHistoryV2(
                    wallet,
                    pin,
                    null,
                    null,
                    50
            ).enqueue(callback);


        }catch (Exception e){

        }

    }
}
