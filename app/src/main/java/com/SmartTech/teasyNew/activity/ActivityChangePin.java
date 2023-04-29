package com.SmartTech.teasyNew.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.SmartTech.teasyNew.Popups;
import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.activity.annotation.Lockable;
import com.SmartTech.teasyNew.activity.annotation.ReturnToActivity;
import com.SmartTech.teasyNew.activity.base.BaseActivity;
import com.SmartTech.teasyNew.activity.transaction.MakeTransactionRunnable2;
import com.SmartTech.teasyNew.activity.transaction.result.TransactionResultData;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.utils.SerializableRunnable;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Response;

@Lockable
@ReturnToActivity(activity = MainActivity.class)
public class ActivityChangePin extends BaseActivity {

    private EditText editTextPin;
    private EditText editTextNewPin;
    private EditText editTextConfirmPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_change_pin);

        editTextPin = findViewById(R.id.editTextNumberPassword3);
        editTextNewPin = findViewById(R.id.editTextNumberPassword3);
        editTextConfirmPin = findViewById(R.id.editTextNumberPassword3);

        findViewById(R.id.btn_proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedClicked();
            }
        });
    }

    private void proceedClicked() {
        String pin = editTextPin.getText().toString();
        String newPin = editTextNewPin.getText().toString();
        String confirmPin = editTextConfirmPin.getText().toString();

        if(StringUtils.isBlank(pin)) {
            Popups.showPopup(this, "Please enter your PIN", null);
            return;
        }

        if(StringUtils.isBlank(newPin)) {
            Popups.showPopup(this, "Please enter your new PIN", null);
            return;
        }

        if(StringUtils.isBlank(confirmPin)) {
            Popups.showPopup(this, "Please, confirm your new PIN", null);
            return;
        }

        String regex = "^[0-9]{4}$";
        if(!pin.matches(regex) || !newPin.matches(regex) || !confirmPin.matches(regex)) {
            Popups.showPopup(this, "PIN must be a 4-digit string", null);
            return;
        }

        ChangePinRunnable runnable = new ChangePinRunnable(session.walletId, pin, newPin);
        runnable.setTargetObject(this);
        runnable.run();
    }

    private static class ChangePinRunnable extends MakeTransactionRunnable2<BaseResponse> {

        private String walletId, pin, newPin;

        private ChangePinRunnable(String walletId, String pin, String newPin) {
            this.walletId = walletId;
            this.pin = pin;
            this.newPin = newPin;
        }

        @Override
        protected Call<BaseResponse> getRequest() {
            return activity.getAppMananagerAPI().pinChangeRequest(
                    walletId,
                    pin,
                    newPin
            );
        }

        @Override
        public SerializableRunnable transactionRetryClicked() {
            return null;
        }

        @Override
        protected void onErrorCode(Response<BaseResponse> response, Intent intent, TransactionResultData transactionResultData) {
            BaseResponse responseBody = response.body();
            switch (responseBody.getResponseCode()) {
                case INCORRECT_CREDENTIALS:
                    Popups.showPopup(activity, "Incorrect PIN", null);
                    return;

                default:
                    Popups.showPopup(activity, "Unsuccessful", null);
                    return;
            }
        }

        @Override
        protected void onFail(Intent intent, TransactionResultData transactionResultData) {
            Popups.showPopup(activity, "Unsuccessful", null);
        }

        @Override
        protected void onSuccess(Response<BaseResponse> response, Intent intent, TransactionResultData transactionResultData) {
            activity.getSession().pin = newPin;
            Dialog dialog = Popups.showPopup(activity, "PIN successfully changed", null);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    activity.onBackPressed();
                }
            });
        }
    }

}
