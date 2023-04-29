package com.SmartTech.teasyNew;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.FRSCPaymentResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.FRSCValidateAppResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.NISValidationResponse;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.codingending.popuplayout.PopupLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Executor;

import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakePaymentInplementation {

    private EditText ApplicationID;
    private MainActivity activity;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private boolean isNIS;
    public MakePaymentInplementation(MainActivity activity, boolean isNIS) {
        this.activity = activity;
        this.isNIS = isNIS;
    }

    public EditText getApplicationID() {
        return ApplicationID;
    }



    private void Verify( String mName, String mApplicationID, String mDLID,
                         String mMobileNumber, String mPaymentStatus, String mServiceType,
                         String mTransactionID, String mValidityDuration, String mTimeStamp,
                         String ButtonName, String amount,String appID, Context context, ViewGroup mGroup){

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout._frscresult, mGroup,false);

        TextView Name, ApplicationID, DLID, MobileNumebr, PaymentStatus,
                ServiceType, TransactionID, ValidityDuration, TimeStamp, confirmButton, allAmount, cancelButton;
        Name = view.findViewById(R.id.textView101);
        ApplicationID = view.findViewById(R.id.textView93);
        DLID = view.findViewById(R.id.textView94);
        MobileNumebr = view.findViewById(R.id.textView95);
        PaymentStatus = view.findViewById(R.id.textView96);
        ServiceType = view.findViewById(R.id.textView97);
        TransactionID = view.findViewById(R.id.textView98);
        ValidityDuration = view.findViewById(R.id.textView99);
        TimeStamp = view.findViewById(R.id.textView100);
        confirmButton = view.findViewById(R.id.textView92);
        allAmount = view.findViewById(R.id.Amount);
        cancelButton = view.findViewById(R.id.textView107);
        PopupLayout popupLayout = PopupLayout.init(context, view);
        Name.setText(mName);
        ApplicationID.setText(mApplicationID);
        DLID.setText(mDLID);
        MobileNumebr.setText(mMobileNumber);
        PaymentStatus.setText(mPaymentStatus);
        ServiceType.setText(mServiceType);
        TransactionID.setText(mTransactionID);
        ValidityDuration.setVisibility(View.GONE);
        ValidityDuration.setText(mValidityDuration);
        TimeStamp.setVisibility(View.GONE);
        TimeStamp.setText(mTimeStamp);
        allAmount.setVisibility(View.GONE);
        allAmount.setText(amount);
        cancelButton.setText(ButtonName);
        if(ButtonName.equals("Close")){
            confirmButton.setVisibility(View.GONE);
        }else{
            confirmButton.setVisibility(View.VISIBLE);
            confirmButton.setText("Confirm");

        }


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();


            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
                FRSCvalidatioPIN(appID);
            }
        });

        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);
    }


    private void FRSCvalidatio(String ApplicationID, Context context, ViewGroup viewGroup){
        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Loading Application Info...");
        Callback<FRSCValidateAppResponse> callback = new Callback<FRSCValidateAppResponse>() {
            @Override
            public void onResponse(Call<FRSCValidateAppResponse> call, Response<FRSCValidateAppResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup("Validation Failed");
                    return;
                }
                FRSCValidateAppResponse responseBody = response.body();
                if(responseBody.getResponseCode() == BaseResponse.ResponseCode.OK){
                    String Name = "APPLICANT NAME \n" + responseBody.applicantFirstname + " " + responseBody.applicantLastname;
                    String buttonText;
                    if(responseBody.paymentStatus.equals("PAID")){
                        buttonText = "Close";
                    }else{
                        buttonText = "Continue";
                    }
                    String mainAmount = Utils.formatBalance(responseBody.amount);
                    String feeAmount = Utils.formatBalance(responseBody.feesAmount);
                    String total = Utils.formatBalance(responseBody.feesAmount + responseBody.amount);
                   String mAmount = "Amount: ₦" + mainAmount + "\n"
                           +"Fee: ₦" + feeAmount + "Total Amount: ₦" + total;
                    Verify(Name,"Application ID\n" +responseBody.applicationID,"DLID\n" + responseBody.DLID,"Mobile Number\n"+responseBody.mobileNumber,
                            "Payment Status\n" + responseBody.paymentStatus, "Service Type\n"+responseBody.serviceTypeID,
                            "Transaction ID\n" + responseBody.transactionID, "Validity Duration\n"+ responseBody.validityDuration,
                            "Date of Application\n"+ responseBody.creationTimestamp,buttonText,mAmount,responseBody.applicationID, context, viewGroup);
                }else{
                    showFailPopup(responseBody.message);
                }




            }

            @Override
            public void onFailure(Call<FRSCValidateAppResponse> call, Throwable t) {
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
        activity.getAppMananagerAPI().frscValidateAppRequest(ApplicationID).enqueue(callback);


    }

    private void FRSCvalidatioPIN(String appID){
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

        checkFingerSensor(fingerPrintbtn,appID);
        fingerPrintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeBio(appID);
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mPin = pin.getOTP();

                //
                if((!mPin.equals(""))&& (mPin.toCharArray().length == 4)) {
                    if(isNIS) {
                        NISMakePayment(appID, mPin);

                    }else{
                        FRSCmMAkePayment(appID, mPin);
                    }
                    confirmkPIN.dismiss();
                }else{
                    pin.showError();
                }
            }
        });

        confirmkPIN.show();
    }

    private void FRSCmMAkePayment(String appID,String mPin){
        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Transaction in Progress...");
        Callback<FRSCPaymentResponse> callback = new Callback<FRSCPaymentResponse>() {
            @Override
            public void onResponse(Call<FRSCPaymentResponse> call, Response<FRSCPaymentResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup("Transaction failed");
                    return;
                }
                TransactionResult myTransaction = new TransactionResult(activity,activity);
                FRSCPaymentResponse responseBody = response.body();
                if(responseBody.getResponseCode() == BaseResponse.ResponseCode.OK){
                    myTransaction.successfulTransaction();
                    myTransaction.getFinancialInstitution().setText("Payment Type");
                    myTransaction.getBankName().setText("FRSC");
                    myTransaction.getTransactionCode().setText(responseBody.txnID);
                    myTransaction.getNarationHeader().setText("Message");
                    myTransaction.getNarration().setText(responseBody.message);
                    myTransaction.getAmountHeader().setVisibility(View.GONE);
                    myTransaction.getAmount().setVisibility(View.GONE);
                    myTransaction.getRescipiantName().setVisibility(View.GONE);
                    myTransaction.getName().setVisibility(View.GONE);
                    myTransaction.getRecipianAc().setVisibility(View.GONE);
                    myTransaction.getAccountNumber().setVisibility(View.GONE);
                    myTransaction.getConfirmkPIN().show();

                }else{
                    showFailPopup(responseBody.message);
                }


            }

            @Override
            public void onFailure(Call<FRSCPaymentResponse> call, Throwable t) {
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
        activity.getAppMananagerAPI().frscPaymentRequest(
                activity.getSession().walletId,
                mPin,
                appID,
                activity.getSession().getAgentShortCode(),
                activity.getSession().getDeviceInfo().getImei(),
                activity.getSession().getDeviceInfo().getAndroidID(),
                activity.getSession().getDeviceInfo().getSimNumber(),
                activity.getSession().getDeviceInfo().getLocationCoordinates())
                .enqueue(callback);


    }

    private void NISValidation(String AppID, Context context, ViewGroup viewGroup){
        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Loading Application Info...");
        Callback<NISValidationResponse> callback = new Callback<NISValidationResponse>() {
            @Override
            public void onResponse(Call<NISValidationResponse> call, Response<NISValidationResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup("Transaction failed");
                    return;
                }
                NISValidationResponse responseBody = response.body();
                if(responseBody.getResponseCode() == BaseResponse.ResponseCode.OK){
                    String Name = "APPLICATION ID \n" + responseBody.appID;
                    String buttonText;
                    if(responseBody.status == 1){
                        buttonText = "Close";
                    }else{
                        buttonText = "Continue";
                    }
                    String mainAmount = Utils.formatBalance(responseBody.amount);
                    String mAmount = "Amount: N" + mainAmount;
                    String message = "Message: " + responseBody.message;
                    String creationTimestamp = responseBody.creationDate;
                    String ExpiringDate = responseBody.expiryDate;
                    String transactionID = responseBody.txnID;
                    String RefNumber = responseBody.refNo;
                    Verify(Name,"Transaction ID\n" + transactionID,"Creation Date\n"+ creationTimestamp,
                            "Expiring Date\n" +ExpiringDate, "Reference Number\n"+RefNumber,
                            "Amount\n" + mAmount, "Message\n"+ message,""
                            ,"",buttonText,"",responseBody.appID, context, viewGroup);
                }else{
                    showFailPopup(responseBody.message);
                }




            }

            @Override
            public void onFailure(Call<NISValidationResponse> call, Throwable t) {
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
        activity.getAppMananagerAPI()
                .nisValidationRequest(AppID)
                .enqueue(callback);

    }


    private void checkFingerSensor(ImageView fingerPrintbtn,String appID ) {
        androidx.biometric.BiometricManager biometricManager =
                androidx.biometric.BiometricManager.from(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {

                // this means we can use biometric sensor
                case BiometricManager.BIOMETRIC_SUCCESS:
                    fingerPrintbtn.setVisibility(View.VISIBLE);
                    executeBio(appID);
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

    private void executeBio(String appID) {
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
                    if(isNIS){
                        NISMakePayment(appID, mPin);
                    }else {
                        FRSCmMAkePayment(appID, mPin);
                    }
                }

            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("TEASYPAY")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        biometricPrompt.authenticate(promptInfo);
    }


    private void NISMakePayment(String AppID, String PIN){
        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Transaction in Progress...");
        Callback<BaseResponse> callback = new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup("Transaction failed");
                    return;
                }
                TransactionResult myTransaction = new TransactionResult(activity,activity);
                BaseResponse responseBody = response.body();
                if(responseBody.getResponseCode() == BaseResponse.ResponseCode.OK){
                    myTransaction.successfulTransaction();
                    myTransaction.getFinancialInstitution().setText("Payment Type");
                    myTransaction.getBankName().setText("NIS");
                    myTransaction.getTransactioncodeHeader().setText("Status");
                    myTransaction.getTransactionCode().setText("PAID");
                    myTransaction.getNarationHeader().setText("Message");
                    myTransaction.getNarration().setText(responseBody.message);
                    myTransaction.getAmountHeader().setVisibility(View.GONE);
                    myTransaction.getAmount().setVisibility(View.GONE);
                    myTransaction.getRescipiantName().setVisibility(View.GONE);
                    myTransaction.getName().setVisibility(View.GONE);
                    myTransaction.getRecipianAc().setVisibility(View.GONE);
                    myTransaction.getAccountNumber().setVisibility(View.GONE);
                    myTransaction.getDirectionHeader().setVisibility(View.GONE);
                    myTransaction.getNarationHeader().setVisibility(View.GONE);

                    myTransaction.getConfirmkPIN().show();

                }else{
                    showFailPopup(responseBody.message);
                }


            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
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
        activity.getAppMananagerAPI().nisPaymentRequest(
                activity.getSession().walletId,
                PIN,
                AppID,
                activity.getSession().getAccountType().name(),
                activity.getSession().getAgentShortCode(),
                activity.getSession().getDeviceInfo().getImei(),
                activity.getSession().getDeviceInfo().getAndroidID(),
                activity.getSession().getDeviceInfo().getSimNumber(),
                activity.getSession().getDeviceInfo().getLocationCoordinates())
                .enqueue(callback);


    }

    private String getDisplayName() {
        Session session = activity.getSession();
        switch (session.getAccountType()) {
            case CUSTOMER:
                String customerFirstName = session.getCustomerFirstName();
                String customerMiddleName = session.getCustomerMiddleName();
                String customerLastName = session.getCustomerLastName();

                String fullName = customerFirstName;
                if(customerMiddleName != null && !"null".equals(customerMiddleName)) {
                    fullName += " " + customerMiddleName;
                }
                fullName += " " + customerLastName;
                return fullName;

            case AGENT:
                return session.getAgentName().replaceAll("[0-9]{6} - ", "");
        }

        return "";
    }


    public PopupLayout applicationID(ViewGroup mGroup, Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frsc_id, mGroup,false);
        TextView Continue = view.findViewById(R.id.textView91);
        ApplicationID = view.findViewById(R.id.editTextTextPersonName3);

        PopupLayout popupLayout = PopupLayout.init(context, view);

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ApplicationID.getText().toString().equals("")){
                    ApplicationID.setError("You Must Enter Application ID");
                    return;
                }
                if(isNIS){
                    NISValidation(ApplicationID.getText().toString().trim(), context, mGroup);
                }else {

                    FRSCvalidatio(ApplicationID.getText().toString(),context, mGroup);
                }
                popupLayout.dismiss();
            }
        });

        return popupLayout;
    }

}
