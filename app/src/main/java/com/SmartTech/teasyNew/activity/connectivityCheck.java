package com.SmartTech.teasyNew.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class connectivityCheck {
    private Context context;
    public connectivityCheck(Context context) {
        this.context = context;
    }

    public boolean checkInterConnection(){
        ConnectivityManager conMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        boolean result = false;
        if (netInfo == null){

            new AlertDialog.Builder(context)
                    .setTitle("No Internet Connection")
                    .setMessage("No Please make sure your phone has internet and try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           checkInterConnection();
                        }
                    }).show();

        }else{
             result = true;
        }

        return result;

    }

}
