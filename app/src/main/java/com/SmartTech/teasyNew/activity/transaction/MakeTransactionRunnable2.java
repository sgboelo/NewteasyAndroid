package com.SmartTech.teasyNew.activity.transaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.SmartTech.teasyNew.Popups;
import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.base.BaseActivity;

import com.SmartTech.teasyNew.activity.transaction.result.TransactionResultData;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.popups.PopupOperationSuccess;
import com.SmartTech.teasyNew.utils.SerializableRunnable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by muddvayne on 11/09/2017.
 */

public abstract class MakeTransactionRunnable2<T extends BaseResponse> extends SerializableRunnable {

    protected transient BaseActivity activity;

    /**
     * This activity will be called when user presses device's or dashboard's back button
     * */

    /**
     * Request to be executed
     * */
    protected abstract Call<T> getRequest();

    /**
     * Runnable that will be executed when 'retry' button clicked in ActivityTransactionResult
     * If not overrided, new instance of this runnable will be returned
     * */
    public abstract SerializableRunnable transactionRetryClicked();

    @Override
    public void run() {
        super.run();

        activity = (BaseActivity) getTargetObject();
        final Dialog operationInProcessDialog = Popups.showOperationInProcessDialog(
                activity,
                getPopupText()
        );

        getRequest().enqueue(new Callback<T>() {

            TransactionResultData transactionResultData = new TransactionResultData();

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                onResult();

                if(!response.isSuccessful() || response.body() == null) {
                    transactionResultData.setResult(TransactionResultData.TransactionResult.FAIL);
                    transactionResultData.setErrorDescription("Internal error");


                    return;
                }

                BaseResponse responseBody = response.body();
                switch (responseBody.getResponseCode()) {
                    case OK:
                        transactionResultData.setPreviousActivityClass(MainActivity.class);
                        transactionResultData.setResult(TransactionResultData.TransactionResult.SUCCESS);
                        transactionResultData.setActivityTitle(activity.getTitle().toString());
                        onSuccess(response,activity.getIntent(), transactionResultData);

                        break;

                    default:

                        transactionResultData.setResult(TransactionResultData.TransactionResult.FAIL);
                        transactionResultData.setErrorDescription(responseBody.message);
                        transactionResultData.setActivityTitle(activity.getTitle().toString());
                        transactionResultData.setButtonOnClickListener(transactionRetryClicked());
                        onFail(activity.getIntent(), transactionResultData);

                        break;
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResult();
                
                transactionResultData.setResult(TransactionResultData.TransactionResult.FAIL);
                transactionResultData.setErrorDescription("Internal error");

            }

            private void onResult() {
                operationInProcessDialog.dismiss();

                Bundle extras = activity.getIntent().getExtras();
                if(extras != null) {

                }

            }
        });
    }

    protected void onSuccess(Response<T> response, Intent intent, TransactionResultData transactionResultData) {
        PopupOperationSuccess success = new PopupOperationSuccess(activity);
        success.setText(transactionResultData.getErrorDescription());
        success.setButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(intent);
                activity.finish();
            }
        });
       success.show();
    }

    protected void onErrorCode(Response<T> response, Intent intent, TransactionResultData transactionResultData) {
        activity.startActivity(intent);
        activity.finish();
    }

    protected void onFail(Intent intent, TransactionResultData transactionResultData) {
        PopupOperationFailed failed = new PopupOperationFailed(activity);
        failed.setText(transactionResultData.getErrorDescription());
        failed.setButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(intent);
                activity.finish();
            }
        });

        failed.show();


    }

    protected String getPopupText() {
        return activity.getString(R.string.transaction_in_process);
    }
}
