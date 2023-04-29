package com.SmartTech.teasyNew;

import android.app.Dialog;
import android.widget.TextView;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.CheckStatusModel;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.TransactionHistoryResponse;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckStatus {
    private List<TransactionHistoryResponse.TransactionHistoryEntry> notificationCache = new ArrayList<>();

    private MainActivity activity;
    private Session session;

    public CheckStatus(MainActivity activity) {
        this.activity = activity;
    }

    public void getStatus(TextView textView, long amount, StepView stepView, TextView generate){
        session = activity.getSession();
        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity,
                "Checking Status Please Wait...");
        Callback<TransactionHistoryResponse> callback = new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {
                sendRequestDialog.dismiss();
                if(!response.isSuccessful()) {
                    onFail();
                    return;
                }
                TransactionHistoryResponse responseBody = response.body();
                notificationCache.addAll(responseBody.transactions);
                String mAmount = String.valueOf(amount);
                if (notificationCache.get(0).amount.equals(mAmount)){
                    textView.setText("Success");
                    stepView.go(1,true);
                    if(generate != null) {
                        generate.setEnabled(true);
                    }

                }else{
                    textView.setText("Failed");
                    stepView.go(0,true);
                    if(generate != null) {
                        generate.setEnabled(false);
                    }
                }




            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {

                onFail();
            }

            private void onFail() {
                sendRequestDialog.dismiss();
            }
        };

        activity.getAppMananagerAPI().transactionHistoryV2(
                session.walletId,
                session.pin,
                null,
                null,
                3
        ).enqueue(callback);



    }



    public void getNIBSSStatus(TextView textView, String sessionID, StepView stepView, String bankCode){
        session = activity.getSession();
        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity,
                "Checking Status Please Wait...");
        Callback<CheckStatusModel> callback = new Callback<CheckStatusModel>() {
            @Override
            public void onResponse(Call<CheckStatusModel> call, Response<CheckStatusModel> response) {
                sendRequestDialog.dismiss();
                if(!response.isSuccessful()) {
                    onFail();
                    return;
                }
                CheckStatusModel responseBody = response.body();
                if(responseBody.getResponseCode() == BaseResponse.ResponseCode.OK){
                    if(responseBody.tgetResponseCode().equals("00")){
                        textView.setText("Bank Settled");
                        stepView.go(2,true);
                        stepView.done(true);
                    }
                }

            }

            @Override
            public void onFailure(Call<CheckStatusModel> call, Throwable t) {
                sendRequestDialog.dismiss();
            }

            private void onFail() {
                sendRequestDialog.dismiss();
            }
        };

        activity.getAppMananagerAPI()
                .getBankStatus(
                        sessionID,
                        bankCode
                ).enqueue(callback);

    }
}
