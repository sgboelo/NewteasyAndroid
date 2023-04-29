package com.SmartTech.teasyNew.activity.transaction;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.SmartTech.teasyNew.AirtimeDataTransactionStatus;
import com.SmartTech.teasyNew.Encrytion;
import com.SmartTech.teasyNew.Popups;
import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.TransactionResult;
import com.SmartTech.teasyNew.Utils;
import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.database;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.model.DataAirtimeModel;
import com.codingending.popuplayout.PopupLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Airtime_Data {
    private MainActivity activity;
    private Context context;
    private int gravity;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private ViewGroup vGroup;
    public Airtime_Data(MainActivity activity, Context context, int gravity, ViewGroup vGroup) {
        this.context = context;
        this.activity = activity;
        this.gravity = gravity;
        this.vGroup = vGroup;
    }

    private void buyAirtime(String Phone, long mAmount,String mPin, String network){
        TransactionResult tResult = new TransactionResult(context, activity);
        long amount = Long.valueOf(mAmount);

        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Transaction in Progress...");
        Callback<BaseResponse> callback = new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup();
                    return;
                }


                BaseResponse responseBody = response.body();
                switch (responseBody.getResponseCode()) {
                    case OK: {
                        Fragment fragment = new AirtimeDataTransactionStatus();
                        Bundle bundle = new Bundle();
                        DataAirtimeModel dataAirtimeModel = new DataAirtimeModel();
                        dataAirtimeModel.setType("airtime");
                        dataAirtimeModel.setNetwork(network);
                        Map<String, String> data = new HashMap<>();
                        data.put("status", "success");
                        data.put("phone", Phone);
                        String amt = String.valueOf(mAmount);
                        data.put("denomination",  amt);
                        dataAirtimeModel.setData(data);
                        bundle.putSerializable("key", dataAirtimeModel);
                        fragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerView,
                                        fragment).commit();
                        break;
                    }
                    default: {
                        showFailPopup();

                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                sendRequestDialog.dismiss();
                showFailPopup();
            }

            private void showFailPopup() {
                Fragment fragment = new AirtimeDataTransactionStatus();
                Bundle bundle = new Bundle();
                DataAirtimeModel dataAirtimeModel = new DataAirtimeModel();
                dataAirtimeModel.setType("airtime");
                dataAirtimeModel.setNetwork(network);
                Map<String, String> data = new HashMap<>();
                data.put("status", "pending");
                data.put("phone", Phone);
                String amt = String.valueOf(mAmount);
                data.put("denomination",  amt);
                dataAirtimeModel.setData(data);
                bundle.putSerializable("key", dataAirtimeModel);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
            }
        };
        activity.getAppMananagerAPI().airtimePurchaseRequest(
                activity.getSession().walletId,
                mPin,
                Phone,
                amount,
                activity.getSession().getDeviceInfo().getImei(),
                activity.getSession().getDeviceInfo().getAndroidID(),
                activity.getSession().getDeviceInfo().getSimNumber(),
                activity.getSession().getDeviceInfo().getLocationCoordinates()
        ).enqueue(callback);

    }

    private void buyData(String Phone, String mPin, String Denomination,
                         String productID,String faceValue, String productDesc, String Network){

        long denomination = (long) (Double.parseDouble(Denomination) * 100);

        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Transaction in progress...");
        Callback<BaseResponse> callback = new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup();
                    return;
                }


                BaseResponse responseBody = response.body();
                if (responseBody.getResponseCode() == BaseResponse.ResponseCode.OK) {/*TransactionResult tResult = new TransactionResult(context, activity);
                        tResult.successfulTransaction();
                        tResult.getNarration().setVisibility(View.GONE);
                        tResult.getAccountNumber().setText(Phone);
                        tResult.getBankName().setText("TeasyMobile");
                        tResult.getAmount().setText(Denomination);
                        tResult.getConfirmkPIN().show();*/
                    Fragment fragment = new AirtimeDataTransactionStatus();
                    Bundle bundle = new Bundle();
                    DataAirtimeModel dataAirtimeModel = new DataAirtimeModel();
                    dataAirtimeModel.setType("data");
                    dataAirtimeModel.setNetwork(Network);
                    Map<String, String> data = new HashMap<>();
                    data.put("status", "success");
                    data.put("phone", Phone);
                    data.put("description", productDesc);
                    data.put("denomination", Denomination);
                    dataAirtimeModel.setData(data);
                    bundle.putSerializable("key", dataAirtimeModel);
                    fragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView,
                                    fragment).commit();
                } else {
                    Fragment fragment = new AirtimeDataTransactionStatus();
                    Bundle bundle = new Bundle();
                    DataAirtimeModel dataAirtimeModel = new DataAirtimeModel();
                    dataAirtimeModel.setType("data");
                    dataAirtimeModel.setNetwork(Network);
                    Map<String, String> data = new HashMap<>();
                    data.put("status", "pending");
                    data.put("phone", Phone);
                    data.put("description", productDesc);
                    data.put("denomination", Denomination);
                    dataAirtimeModel.setData(data);
                    bundle.putSerializable("key", dataAirtimeModel);
                    fragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView,
                                    fragment).commit();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                sendRequestDialog.dismiss();
                showFailPopup();
            }

            private void showFailPopup() {
                Fragment fragment = new  AirtimeDataTransactionStatus();
                Bundle bundle = new Bundle();
                DataAirtimeModel dataAirtimeModel = new DataAirtimeModel();
                dataAirtimeModel.setType("data");
                dataAirtimeModel.setNetwork(Network);
                Map<String,String> data = new HashMap<>();
                data.put("status", "pending");
                data.put("phone",Phone);
                data.put("description", productDesc);
                data.put("denomination", Denomination);
                dataAirtimeModel.setData(data);
                bundle.putSerializable("key", dataAirtimeModel);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
            }
        };
        activity.getAppMananagerAPI().paDataPurchaseRequest(
                activity.getSession().walletId,
                mPin,
                Phone,
                productID,
                denomination,
                faceValue,
                activity.getSession().getDeviceInfo().getAndroidID(),
                activity.getSession().getDeviceInfo().getSimNumber(),
                activity.getSession().getDeviceInfo().getLocationCoordinates()
        ).enqueue(callback);

    }


    private String format(String Amount){

        return  "₦" + Utils.formatBalance(String.valueOf(Amount));
    }

    private void checkFingerSensor(ImageView fingerprint, String Phone, long mAmount) {
        androidx.biometric.BiometricManager biometricManager =
                androidx.biometric.BiometricManager.from(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {

                // this means we can use biometric sensor
                case BiometricManager.BIOMETRIC_SUCCESS:
                    fingerprint.setVisibility(View.VISIBLE);
                    executeBio(Phone,mAmount);
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

    private void executeBio(String Phone, long mAmount) {
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
                    buyAirtime(Phone,mAmount,mPin,"");
                    //authorize(mPin);
                }

            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("TEASYPAY")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        biometricPrompt.authenticate(promptInfo);
    }

    private void checkFingerSensor(ImageView fingerprint,String Phone, String Denomination,
                                   String productID,String faceValue,Dialog dialog) {
        androidx.biometric.BiometricManager biometricManager =
                androidx.biometric.BiometricManager.from(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {

                // this means we can use biometric sensor
                case BiometricManager.BIOMETRIC_SUCCESS:
                    fingerprint.setVisibility(View.VISIBLE);
                    executeBio(Phone, Denomination,
                            productID,faceValue,dialog);
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

    private void executeBio(String Phone, String Denomination,
                            String productID,String faceValue, Dialog dialog) {
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
                    buyData(Phone, mPin, Denomination,
                            productID, faceValue , "", "");
                    //authorize(mPin);
                }

            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("TEASYPAY")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        biometricPrompt.authenticate(promptInfo);
    }


    public void airtimePIN(String Phone, long mAmount, String network){
        final String[] test = {""};
        LayoutInflater inflater = LayoutInflater.from(vGroup.getContext());
        View view = inflater.inflate(R.layout.pin_interface, vGroup,false);
        OtpTextView OTP = view.findViewById(R.id.firstPinView);
        TextView one = view.findViewById(R.id.textView1992);
        ImageButton back = view.findViewById(R.id.imageButton11);
        TextView two = view.findViewById(R.id.textView200e);
        TextView three = view.findViewById(R.id.textViewv201);
        TextView four = view.findViewById(R.id.textViewq199);
        TextView five = view.findViewById(R.id.textView2002);
        TextView six = view.findViewById(R.id.textView2r01);
        TextView seven = view.findViewById(R.id.textView199);
        TextView eight = view.findViewById(R.id.textView2e00);
        TextView nine = view.findViewById(R.id.textView201);
        TextView zero = view.findViewById(R.id.textView200);
        ImageButton close = view.findViewById(R.id.imageButton13);


        PopupLayout popupLayout = PopupLayout.init(vGroup.getContext(), view);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "1";
                OTP.setOTP(test[0]);

            }
        });

        OTP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        OTP.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                if(test[0].length() == 4){
                    if(OTP.getOTP().equals(activity.getSession().pin)) {

                        popupLayout.dismiss();
                        buyAirtime(Phone,mAmount,OTP.getOTP(), network);
                    }else {
                        Popups.showPopup(activity, "You Entered Incorrect PIN.\nCheck your PIN and Try Again",
                                "Incorrect PIN Entered");
                    }
                }
            }

            @Override
            public void onOTPComplete(String otp) {

            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "2";
                OTP.setOTP(test[0]);
            }
        });


        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "3";
                OTP.setOTP(test[0]);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "4";
                OTP.setOTP(test[0]);
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "5";
                OTP.setOTP(test[0]);
            }
        });


        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "6";
                OTP.setOTP(test[0]);
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "7";
                OTP.setOTP(test[0]);
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "8";
                OTP.setOTP(test[0]);
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "9";
                OTP.setOTP(test[0]);
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "0";
                OTP.setOTP(test[0]);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(test[0].length() < 1){
                    return;
                }
                test[0] = test[0].substring(0, test[0].length()-1);
                OTP.setOTP(test[0]);
            }
        });
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void dataPINRQ(String Phone, String Denomination,
                          String productID, String faceValue, String productDesc, String Network){
        final String[] test = {""};
        LayoutInflater inflater = LayoutInflater.from(vGroup.getContext());
        View view = inflater.inflate(R.layout.pin_interface, vGroup,false);
        OtpTextView OTP = view.findViewById(R.id.firstPinView);
        TextView one = view.findViewById(R.id.textView1992);
        ImageButton back = view.findViewById(R.id.imageButton11);
        TextView two = view.findViewById(R.id.textView200e);
        TextView three = view.findViewById(R.id.textViewv201);
        TextView four = view.findViewById(R.id.textViewq199);
        TextView five = view.findViewById(R.id.textView2002);
        TextView six = view.findViewById(R.id.textView2r01);
        TextView seven = view.findViewById(R.id.textView199);
        TextView eight = view.findViewById(R.id.textView2e00);
        TextView nine = view.findViewById(R.id.textView201);
        TextView zero = view.findViewById(R.id.textView200);
        ImageButton close = view.findViewById(R.id.imageButton13);


        PopupLayout popupLayout = PopupLayout.init(vGroup.getContext(), view);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
            }
        });


        OTP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        OTP.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                if(otp.equals(activity.getSession().pin)) {
                    popupLayout.dismiss();
                    buyData(Phone, OTP.getOTP(), Denomination,
                            productID, faceValue, productDesc, Network);
                }else{
                    Popups.showPopup(activity, "You Entered Incorrect PIN.\nCheck your PIN and Try Again",
                            "Incorrect PIN Entered");
                }

                popupLayout.dismiss();
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "1";
                OTP.setOTP(test[0]);

            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "2";
                OTP.setOTP(test[0]);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "3";
                OTP.setOTP(test[0]);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "4";
                OTP.setOTP(test[0]);
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "5";
                OTP.setOTP(test[0]);
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "6";
                OTP.setOTP(test[0]);
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "7";
                OTP.setOTP(test[0]);
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "8";
                OTP.setOTP(test[0]);
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "9";
                OTP.setOTP(test[0]);
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(test[0].length() < 4)){
                    return;
                }
                test[0] += "0";
                OTP.setOTP(test[0]);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(test[0].length() < 1){
                    return;
                }
                test[0] = test[0].substring(0, test[0].length()-1);
                OTP.setOTP(test[0]);
            }
        });
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);
    }

}


