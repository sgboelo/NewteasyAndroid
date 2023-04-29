package com.SmartTech.teasyNew.activity;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.SmartTech.teasyNew.Encrytion;
import com.SmartTech.teasyNew.Popups;
import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.Session;
import com.SmartTech.teasyNew.TransactionResult;
import com.SmartTech.teasyNew.Utils;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetUserDataResponse;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.view.AmountInputField;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Executor;

import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferToTeasy {

    private Context context;
    private MainActivity mainActivity;
    private Session session;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private Dialog quickDialog;
    public TransferToTeasy(Context context, MainActivity mainActivity, Session session) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.session = session;
    }


    private void transferToWallet(EditText destinationNumberET, AmountInputField AmounintF) {
        final String destNumber = destinationNumberET.getText().toString().replaceAll("[^0-9]","");
        final long amount = AmounintF.getValueInCents();

        if(StringUtils.isBlank(destNumber)) {
            destinationNumberET.setError("Please, enter wallet number");
            return;
        }
        if(amount < 1) {
            AmounintF.setError("Please, enter amount");
            return;
        }

      String sourceNumber = Utils.phoneToInternational(session.walletId);
        if(Utils.phoneToInternational(destNumber).equals(sourceNumber)) {
            destinationNumberET.setError("Transfer to self is not allowed");
            return;
        }

       Dialog sendRequestDialog = Popups.showOperationInProcessDialog(mainActivity,"Validating Receivers Account...");

        Callback<GetUserDataResponse> callback = new Callback<GetUserDataResponse>() {
            @Override
            public void onResponse(Call<GetUserDataResponse> call, Response<GetUserDataResponse> response) {
                sendRequestDialog.dismiss();
                if(!response.isSuccessful()) {
                    showFailPopup("Validation failed");
                    return;
                }

                GetUserDataResponse responseBody = response.body();
                switch (responseBody.getResponseCode()) {
                    case OK:
                        String accountType = responseBody.accountType;
                        String receiverName;

                        switch (accountType) {
                            case "AGENT":
                                receiverName = responseBody.organizationName;
                                break;
                            default:
                                receiverName = responseBody.firstName;

                                String middleName = responseBody.middleName;
                                if (!StringUtils.isBlank(middleName)) {
                                    receiverName += " " + middleName;
                                }

                                receiverName += " " + responseBody.lastName;
                                break;
                        }

                        confirm(receiverName,destNumber,String.valueOf(amount));
                        break;

                    case CUSTOMER_NOT_REGISTERED:

                        showFailPopup("Customer Not Registered");
                        break;

                    default:
                        showFailPopup(responseBody.message);
                        break;
                }
            }

            @Override
            public void onFailure(Call<GetUserDataResponse> call, Throwable t) {
                sendRequestDialog.dismiss();
                showFailPopup("Validation failed");
            }

            private void showFailPopup(String message) {
                PopupOperationFailed popup = new PopupOperationFailed(context);
                if(!StringUtils.isBlank(message)) {
                    popup.setText(message);
                }
                popup.show();
            }
        };

        mainActivity.getAppMananagerAPI().getUserDataRequest(destNumber).enqueue(callback);
    }


    public void Transfer(){

        quickDialog = new Dialog(mainActivity);
        quickDialog.setContentView(R.layout.teasy_to_teasy);
        Window window = quickDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        quickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText mobileNumber = quickDialog.findViewById(R.id.editTextTextPersonName);
        AmountInputField Amount = quickDialog.findViewById(R.id.editTextNumberDecimal);//textView27
        TextView Tf = quickDialog.findViewById(R.id.textView27);
        ImageButton closeBTN = quickDialog.findViewById(R.id.imageButton8);
        Tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(Amount.getValueInCents()>0)){
                    Amount.setError("Invalid Amount Entered");
                    return;
                }
                quickDialog.dismiss();
                transferToWallet(mobileNumber, Amount);
            }
        });

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickDialog.dismiss();
            }
        });
        quickDialog.show();
    }


    private void confirm(String mName, String Number, String mAmount){
        quickDialog = new Dialog(context);
        quickDialog.setContentView(R.layout.confirm_teasy_pop);
        Window window = quickDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        quickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView Name = quickDialog.findViewById(R.id.textView32);
        TextView mobileNumber = quickDialog.findViewById(R.id.textView33);
        TextView Amount = quickDialog.findViewById(R.id.textView34);//textView27
        TextView Confirm = quickDialog.findViewById(R.id.textView36);
        TextView Cancel = quickDialog.findViewById(R.id.textView35);
        ImageButton close = quickDialog.findViewById(R.id.imageButton9);
        Name.setText(mName);
        mobileNumber.setText(Number);
        Amount.setText(format(mAmount));
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickDialog.dismiss();
            }
        });
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickDialog.dismiss();
                confirmPIN(mAmount, Number);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickDialog.dismiss();
            }
        });
        quickDialog.show();
    }

    private void confirmPIN(String Amount, String Number){
        quickDialog = new Dialog(context);
        quickDialog.setContentView(R.layout.pin_request_pop);
        Window window = quickDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        quickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        OtpTextView pin = quickDialog.findViewById(R.id.firstPinView);
        TextView ok = quickDialog.findViewById(R.id.textView38);
        ImageView imageView = quickDialog.findViewById(R.id.imageView20);
        checkFingerSensor(imageView,Amount, Number);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeBio(Amount,Number);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickDialog.dismiss();
                transferFund(Number, Amount,pin.getOTP());
            }
        });


        quickDialog.show();

    }



    private void transferFund(String Phone, String mAmount,String mPin){

        long amount = Long.valueOf(mAmount);

        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(mainActivity, "Verifying Name and Balance");
        Callback<BaseResponse> callback = new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup("Validation failed");
                    return;
                }


                BaseResponse responseBody = response.body();
                switch (responseBody.getResponseCode()) {
                    case OK: {
                        TransactionResult tResult = new TransactionResult(context,mainActivity);
                        tResult.successfulTransaction();
                        tResult.getBankName().setText("TeasyMobile");
                        tResult.getAmount().setText("₦"+Utils.formatBalance(mAmount));
                        tResult.getAccountNumber().setText(Phone);
                        tResult.getNarationHeader().setVisibility(View.GONE);
                        tResult.getRescipiantName().setVisibility(View.GONE);
                        tResult.getName().setVisibility(View.GONE);
                        tResult.getNarration().setVisibility(View.GONE);
                        tResult.getAmountHeader().setVisibility(View.GONE);
                        tResult.getTransactionCode().setVisibility(View.GONE);

                        tResult.getDirectionHeader().setVisibility(View.GONE);
                        tResult.getTransactioncodeHeader().setVisibility(View.GONE);
                        tResult.getDirection().setVisibility(View.GONE);
                        tResult.getConfirmkPIN().show();
                        break;
                    }
                    default:
                        showFailPopup(responseBody.message);
                        break;
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                sendRequestDialog.dismiss();
                showFailPopup("Validation failed");
            }

            private void showFailPopup(String message) {
                PopupOperationFailed popup = new PopupOperationFailed(context);
                if (!StringUtils.isBlank(message)) {
                    popup.setText(message);
                }
                popup.show();
            }
        };

        mainActivity.getAppMananagerAPI().walletTransferRequest(
                mainActivity.getSession().walletId,
                mPin,
                Phone,
                amount,
                mainActivity.getSession().getAccountType().name(),
                mainActivity.getSession().getAgentShortCode(),
                mainActivity.getSession().getDeviceInfo().getImei(),
                mainActivity.getSession().getDeviceInfo().getAndroidID(),
                mainActivity.getSession().getDeviceInfo().getSimNumber(),
                mainActivity.getSession().getDeviceInfo().getLocationCoordinates()
        ).enqueue(callback);

    }

    private String format(String Amount){

        return  "₦" + Utils.formatBalance(String.valueOf(Amount));
    }


    private void checkFingerSensor(ImageView fingerprint,String Amount, String Number) {
        androidx.biometric.BiometricManager biometricManager =
                androidx.biometric.BiometricManager.from(mainActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {

                // this means we can use biometric sensor
                case BiometricManager.BIOMETRIC_SUCCESS:
                    fingerprint.setVisibility(View.VISIBLE);
                    executeBio(Amount, Number);
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
                    fingerprint.setVisibility(View.GONE);
                }
            }
        }
    }

    private void executeBio(String Amount, String Number) {
        executor = ContextCompat.getMainExecutor(mainActivity);
        biometricPrompt = new BiometricPrompt(mainActivity, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {

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

                if(!(new database(mainActivity).getLEV() < 1)){
                    String encryptedString = new database(mainActivity).getTPin();
                    String mPin = Encrytion.decrypt(encryptedString);
                    transferFund(Number, Amount,mPin);
                    //authorize(mPin);
                }

            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("TEASYPAY")
                .setDescription("Use Your Fingerprint to Login ").setNegativeButtonText("Cancel").build();


        biometricPrompt.authenticate(promptInfo);
    }
}
