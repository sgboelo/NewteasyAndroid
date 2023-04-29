package com.SmartTech.teasyNew;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.biometrics.BiometricManager;
import android.location.Address;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.database;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AEDCGetMeterInfoResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AEDCPurchaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetInterswitchPaymentItemsResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.InterswitchPaymentResponse;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.view.AmountInputField;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class confirm_purchase {
    private String Description, Biller, meterdetails, mMeterNO,
            PhoneNumber, billerID, payMentCode, itemName, displayAmount, category, address, name;
    private Dialog myDialog;
    private MainActivity activity;
    private long amount;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    public confirm_purchase(MainActivity activity) {
        this.activity = activity;
    }

    public void setBillerID(String billerID) {
        this.billerID = billerID;
    }

    public void setDisplayAmount(String displayAmount) {
        this.displayAmount = displayAmount;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPayMentCode(String payMentCode) {
        this.payMentCode = payMentCode;
    }

    public void setmMeterNO(String mMeterNO) {
        this.mMeterNO = mMeterNO;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setBiller(String biller) {
        Biller = biller;
    }

    public void setMeterdetails(String meterdetails) {
        this.meterdetails = meterdetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void Confirm_Purchase(Dialog newDialog){
        myDialog = new Dialog(activity);
        myDialog.setContentView(R.layout.confirm_puchase);
        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView description, billerName, _meterdetails, cancel, confirm, mmamount;

        description = myDialog.findViewById(R.id.textView86);
        billerName = myDialog.findViewById(R.id.textView88);
        _meterdetails = myDialog.findViewById(R.id.textView87);
        confirm = myDialog.findViewById(R.id.textView90);
        cancel =myDialog.findViewById(R.id.textView89);
        mmamount = myDialog.findViewById(R.id.textView85);
        myDialog.getWindow().getAttributes().windowAnimations = R.anim.bottom_up;
        newDialog.dismiss();
    if(category.equals("ELECTRICITY")) {
        description.setText(Biller);
        billerName.setText(itemName);
        _meterdetails.setText("Meter Number:\n"+mMeterNO);
        mmamount.setText("Amount:\n"+ "N"+displayAmount);
    }else if(category.equals("CABLE_TV")){

        String temp = Utils.formatBalance(amount);
        description.setText(Biller);
        billerName.setText("Product Name:\n"+itemName);
        _meterdetails.setText("SmartCard Number:\n"+mMeterNO);
        mmamount.setText("Amount:\n"+ "N"+temp);
    }else{
        String temp = Utils.formatBalance(amount);
        description.setText(Biller);
        billerName.setText("Product Name:\n"+itemName);
        _meterdetails.setText("Modem Number:\n"+mMeterNO);
        mmamount.setText("Amount:\n"+ "₦"+temp);
    }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PinConfirmationInterSwitch(payMentCode, billerID, amount,mMeterNO);
                myDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    public void Confirm_PurchaseAEDC(Dialog newDialog){
        myDialog = new Dialog(activity);
        myDialog.setContentView(R.layout.confirm_puchase);
        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView description, billerName, _meterdetails, cancel, confirm;
        description = myDialog.findViewById(R.id.textView86);
        billerName = myDialog.findViewById(R.id.textView88);
        _meterdetails = myDialog.findViewById(R.id.textView87);
        confirm = myDialog.findViewById(R.id.textView90);
        cancel =myDialog.findViewById(R.id.textView89);
        myDialog.findViewById(R.id.textView85).setVisibility(View.GONE);
        myDialog.getWindow().getAttributes().windowAnimations = R.anim.bottom_up;
        newDialog.dismiss();
        description.setText(Description);
        billerName.setText(Biller);
        _meterdetails.setText(meterdetails);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                confirmationPin(mMeterNO, amount, PhoneNumber);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    private void purchaseAEDC(String meterNumber, long amount, String pin, String RCPhoneNumber){

        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Transaction in progress...");
        Callback<AEDCPurchaseResponse> callback = new Callback<AEDCPurchaseResponse>() {
            @Override
            public void onResponse(Call<AEDCPurchaseResponse> call, Response<AEDCPurchaseResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup("Transaction failed");
                    return;
                }

                AEDCPurchaseResponse responseBody = response.body();
                String temp = Utils.formatBalance(amount);
                assert responseBody != null;
                TransactionResult tResult = new TransactionResult(activity,activity);
                if(responseBody.getResponseCode() == BaseResponse.ResponseCode.OK) {

                    tResult.successfulTransaction();
                    tResult.getAccountNumber().setText(mMeterNO);
                    tResult.getRescipiantName().setText("Meter Name");
                    tResult.getName().setText(name.toUpperCase());
                    tResult.getFinancialInstitution().setText("Payment Type");
                    tResult.getBankName().setText("Electricity");
                    tResult.getTransactioncodeHeader().setText("Token(Units)");
                    tResult.getTransactionCode().setText("Token: "+responseBody.token +" (Units: "+responseBody.units+")");
                    tResult.getNarationHeader().setText("Meter Address");
                    tResult.getNarration().setText(address);
                    tResult.getAmount().setText("₦"+temp);
                    tResult.getDirection().setText("Debit");
                    tResult.getConfirmkPIN().show();

                }else{
                    tResult.failedTransaction(responseBody.message);
                }


            }

            @Override
            public void onFailure(Call<AEDCPurchaseResponse> call, Throwable t) {
                sendRequestDialog.dismiss();
                showFailPopup("Validation failed");
            }

            private void showFailPopup(String message) {
                PopupOperationFailed popup = new PopupOperationFailed(activity);
                if (!StringUtils.isBlank(message)) {
                    popup.setText(message);
                }
                popup.show();
            }
        };
                activity.getAppMananagerAPI().aedcPurchaseRequest(
                activity.getSession().walletId,
                pin,
                meterNumber,
                amount,
                activity.getSession().getAccountType().name(),
                activity.getSession().getAgentShortCode(),
                RCPhoneNumber,
                activity.getSession().getDeviceInfo().getImei(),
                activity.getSession().getDeviceInfo().getAndroidID(),
                activity.getSession().getDeviceInfo().getSimNumber(),
                activity.getSession().getDeviceInfo().getLocationCoordinates())
                .enqueue(callback);


    }




    private void confirmationPin(String meterNumber, long amount, String RCPhoneNumber){
        OtpTextView pin;
        TextView Ok;
        ImageView fingerPrintbtn;
        Dialog confirmkPIN = new Dialog(activity);
        confirmkPIN.setContentView(R.layout.pin_request_pop);
        Window window = confirmkPIN.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmkPIN.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pin = confirmkPIN.findViewById(R.id.firstPinView);
        Ok = confirmkPIN.findViewById(R.id.textView38);
        fingerPrintbtn = confirmkPIN.findViewById(R.id.imageView20);
        checkFingerSensor(fingerPrintbtn,meterNumber, amount,RCPhoneNumber);
        fingerPrintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                executeBio(meterNumber, amount, RCPhoneNumber);
                confirmkPIN.dismiss();
            }
        });
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mPin = pin.getOTP();
                //
                if((!mPin.equals(""))&& (mPin.toCharArray().length == 4)) {
                    myDialog.dismiss();
                    purchaseAEDC(meterNumber, amount, mPin, RCPhoneNumber);
                    confirmkPIN.dismiss();
                }else{
                    pin.showError();
                }
            }
        });

        confirmkPIN.show();
    }

    private void payBill(String paymentCode, String billerID, String ID, long Pamount){
        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Transaction in progress...");
        Callback<InterswitchPaymentResponse> callback = new Callback<InterswitchPaymentResponse>() {
            @Override
            public void onResponse(Call<InterswitchPaymentResponse> call, Response<InterswitchPaymentResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup("Transaction failed");
                    return;
                }


                //ArrayList<GetInterswitchPaymentItemsResponse.InterswitchPaymentItem> arrayList = new ArrayList<>();
                InterswitchPaymentResponse responseBody = response.body();
                assert responseBody != null;
                TransactionResult tResult = new TransactionResult(activity,activity);
                if(responseBody.getResponseCode() == BaseResponse.ResponseCode.OK) {

                    try {
                        if (category.equals("ELECTRICITY")) {
                            tResult.successfulTransaction();
                            tResult.getAccountNumber().setText(mMeterNO);
                            tResult.getFinancialInstitution().setText("Transaction Type");
                            tResult.getBankName().setText("Electricity");
                            tResult.getTransactioncodeHeader().setText("Token");
                            tResult.getTransactionCode().setText(responseBody.txnID);
                            tResult.getNarration().setText(responseBody.message);
                            tResult.getDirection().setText("Debit");
                            tResult.getConfirmkPIN().show();


                        } else {
                            tResult.successfulTransaction();
                            tResult.getName().setVisibility(View.GONE);
                            tResult.getRescipiantName().setVisibility(View.GONE);
                            tResult.getAmount().setVisibility(View.GONE);
                            tResult.getAmountHeader().setVisibility(View.GONE);
                            tResult.getRecipianAc().setVisibility(View.GONE);
                            tResult.getAccountNumber().setVisibility(View.GONE);
                            tResult.getFinancialInstitution().setText("Payment Type");
                            tResult.getBankName().setText("TV");
                            tResult.getTransactionCode().setText(responseBody.txnID);
                            tResult.getNarration().setText(responseBody.message);
                            tResult.getConfirmkPIN().show();
                        }
                    }catch (Exception e){

                    }

                }else{
                    tResult.failedTransaction(responseBody.message);
                }

            }

            @Override
            public void onFailure(Call<InterswitchPaymentResponse> call, Throwable t) {
                sendRequestDialog.dismiss();
                showFailPopup("Transaction failed");
            }

            private void showFailPopup(String message) {
                PopupOperationFailed popup = new PopupOperationFailed(activity);
                if (!StringUtils.isBlank(message)) {
                    popup.setText(message);
                }
                popup.show();
            }
        };
        activity.getAppMananagerAPI().payInterswitchBiller(
                activity.getSession().walletId,
                activity.getSession().pin,
                billerID,
                ID,
                Pamount,
                paymentCode,
                activity.getSession().getAgentShortCode(),
                activity.getSession().getDeviceInfo().getImei(),
                activity.getSession().getDeviceInfo().getAndroidID(),
                activity.getSession().getDeviceInfo().getSimNumber(),
                activity.getSession().getDeviceInfo().getLocationCoordinates()
        ).enqueue(callback);

    }

    private void PinConfirmationInterSwitch(String payMentCode, String bill_ID, long _amount, String _ID){
        OtpTextView pin;
        TextView Ok;
        ImageView imageView;
        Dialog confirmkPIN = new Dialog(activity);
        confirmkPIN.setContentView(R.layout.pin_request_pop);
        Window window = confirmkPIN.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmkPIN.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pin = confirmkPIN.findViewById(R.id.firstPinView);
        Ok = confirmkPIN.findViewById(R.id.textView38);
        imageView = confirmkPIN.findViewById(R.id.imageView20);
        checkFingerSensorInterSwitch(imageView, payMentCode, bill_ID, _amount, _ID);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                executeBioInterSwitch(payMentCode, bill_ID, _amount, _ID);
                confirmkPIN.dismiss();
            }
        });
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mPin = pin.getOTP();

                //
                if((!mPin.equals(""))&& (mPin.toCharArray().length == 4)) {
                    myDialog.dismiss();
                    payBill(payMentCode,bill_ID,_ID,_amount);
                    confirmkPIN.dismiss();
                }else{
                    pin.showError();
                }
            }
        });

        confirmkPIN.show();
    }



    private void checkFingerSensorInterSwitch(ImageView fingerPrintbtn, String payMentCode, String bill_ID, long _amount, String _ID) {
        androidx.biometric.BiometricManager biometricManager =
                androidx.biometric.BiometricManager.from(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {

                // this means we can use biometric sensor
                case BiometricManager.BIOMETRIC_SUCCESS:
                    fingerPrintbtn.setVisibility(View.VISIBLE);
                    executeBioInterSwitch(payMentCode, bill_ID, _amount, _ID);
                    break;

                // this means that the device doesn't have fingerprint sensor
               /* case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    fingerPrintbtn.setVisibility(View.GONE);
                    break;

                // this means that biometric sensor is not available
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    fingerPrintbtn.setVisibility(View.GONE);
                    break;

                // this means that the device doesn't contain your fingerprint
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    fingerPrintbtn.setVisibility(View.GONE);
                    break;

                */
                default:{
                    fingerPrintbtn.setVisibility(View.GONE);
                }
            }
        }
    }

    private void executeBioInterSwitch(String payMentCode, String bill_ID, long _amount, String _ID) {
        executor = ContextCompat.getMainExecutor(activity);
        biometricPrompt = new BiometricPrompt(activity, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                if(!(new database(activity).getLEV() < 1)){
                    String encryptedString = new database(activity).getTPin();
                    String mPin = Encrytion.decrypt(encryptedString);
                    payBill(payMentCode,bill_ID,_ID,_amount);

                    //authorize(mPin);
                }

            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("TEASYPAY")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        biometricPrompt.authenticate(promptInfo);
    }

    private void checkFingerSensor(ImageView fingerPrintbtn,String meterNumber, long amount, String RCPhoneNumber) {
        androidx.biometric.BiometricManager biometricManager =
                androidx.biometric.BiometricManager.from(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {

                // this means we can use biometric sensor
                case BiometricManager.BIOMETRIC_SUCCESS:
                    fingerPrintbtn.setVisibility(View.VISIBLE);
                    executeBio(meterNumber, amount, RCPhoneNumber);
                    break;

                // this means that the device doesn't have fingerprint sensor
               /* case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    fingerPrintbtn.setVisibility(View.GONE);
                    break;

                // this means that biometric sensor is not available
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    fingerPrintbtn.setVisibility(View.GONE);
                    break;

                // this means that the device doesn't contain your fingerprint
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    fingerPrintbtn.setVisibility(View.GONE);
                    break;

                */
                default:{
                    fingerPrintbtn.setVisibility(View.GONE);
                }
            }
        }
    }

    private void executeBio(String meterNumber, long amount, String RCPhoneNumber) {
        executor = ContextCompat.getMainExecutor(activity);
        biometricPrompt = new BiometricPrompt(activity, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                if(!(new database(activity).getLEV() < 1)){
                    String encryptedString = new database(activity).getTPin();
                    String mPin = Encrytion.decrypt(encryptedString);
                    purchaseAEDC(meterNumber, amount, mPin, RCPhoneNumber);
                    //authorize(mPin);
                }

            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("TEASYPAY")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        biometricPrompt.authenticate(promptInfo);
    }

}
