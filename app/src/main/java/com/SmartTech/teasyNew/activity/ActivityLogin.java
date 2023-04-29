package com.SmartTech.teasyNew.activity;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.SmartTech.teasyNew.BuildConfig;
import com.SmartTech.teasyNew.Encrytion;
import com.SmartTech.teasyNew.FontsOverride;
import com.SmartTech.teasyNew.LoadContacts;
import com.SmartTech.teasyNew.PermissionException;
import com.SmartTech.teasyNew.Popups;
import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.Session;
import com.SmartTech.teasyNew.Support;
import com.SmartTech.teasyNew.activity.base.BaseActivity;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AuthResponse;
import com.SmartTech.teasyNew.model.ContactModel;
import com.SmartTech.teasyNew.model.DeviceInfo;
import com.SmartTech.teasyNew.view.PinView;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Response;

/**
 * Updated by cracklord on 17/10/2023.
 */

public class ActivityLogin extends BaseActivity {

    private static final Logger logger = Logger.getLogger(ActivityLogin.class.getCanonicalName());

    private static final int WALLET_ID_LENGTH = 11;
    private static final int PIN_LENGTH = 4;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String walletID, pin = "";
    private EnterMode enterMode;
    private TextView textView;
    private Button btnChangeID;
    private ImageButton fingerPrintbtn;
    private PinView pinView;
    private OtpTextView Number, Pin;
    private Button btnAccept;
    private Dialog exitDialog;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    UpdateManager mUpdateManager;
    private static final int RC_APP_UPDATE = 11;
    //private BiometricPrompt.PromtInfo promptInfo;
    private ArrayList<ContactModel> mContacts;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Muli-Light.ttf");
        setContentView(R.layout.activity_login);
        executor = ContextCompat.getMainExecutor(this);

        // Creates instance of the manager.
        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.IMMEDIATE);
        mUpdateManager.start();

        if (session == null) {
            session = new Session();
        }
        LoadContacts mload = new LoadContacts(this);
        mContacts = mload.getAllContacts() ;

        DeviceInfo deviceInfo;

        try {
            deviceInfo = DeviceInfo.newInstance(ActivityLogin.this);
            session.setDeviceInfo(deviceInfo);
        } catch (PermissionException e) {
            logger.log(Level.WARNING, "", e);
        }
        pinView = findViewById(R.id.animation);
        btnAccept = findViewById(R.id.btn_accept);

        sharedPref = getSharedPreferences("prefs", MODE_PRIVATE);
        editor = sharedPref.edit();

        walletID = sharedPref.getString("walletID", "");
        enterMode = (walletID.length() < 1) ? EnterMode.ENTER_WALLET_ID : EnterMode.ENTER_PIN;
        Number = findViewById(R.id.phoneNumber);
        Pin = findViewById(R.id.firstPinView);
        textView = findViewById(R.id.textView105);
        btnChangeID = findViewById(R.id.btn_change_id);

        fingerPrintbtn = findViewById(R.id.fingerPrintBTN);
        switch (enterMode) {
            case ENTER_WALLET_ID:
                Number.setVisibility(View.VISIBLE);
                Pin.setVisibility(View.GONE);
                btnChangeID.setText("Register");
                textView.setText("Enter Wallet ID \n(Registered Phone Number)");
                Number.requestFocusOTP();

                break;
            case ENTER_PIN:
                Pin.setVisibility(View.VISIBLE);
                Number.setVisibility(View.GONE);
                btnAccept.setVisibility(View.INVISIBLE);
                textView.setText("Enter PIN");
                btnChangeID.setText("Change ID");
                Pin.requestFocusOTP();
                break;
        }


        Number.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                if (Number.getOTP().length() == WALLET_ID_LENGTH) {
                    setAcceptButtonEnabled(true);
                    walletID = Number.getOTP();
                    Number.setVisibility(View.GONE);
                    Pin.setVisibility(View.VISIBLE);
                    Pin.requestFocusOTP();
                    textView.setText("Enter PIN");
                    btnChangeID.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onOTPComplete(String otp) {

            }
        });

        Pin.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                if (Pin.getOTP().length() <= PIN_LENGTH) {
                    pinView.setVisibleDots(PIN_LENGTH);


                }
            }

            @Override
            public void onOTPComplete(String otp) {
                if (Pin.getOTP().length() == PIN_LENGTH) {
                    pin = Pin.getOTP();
                    Pin.setVisibility(View.GONE);
                    pinView.setVisibility(View.VISIBLE);
                    pinView.startAnim();
                    authorize(pin);
                }
            }
        });
        fingerPrintbtn.setVisibility(View.GONE);
        if(!(new database(this).getLEV() < 1)) {
            String encryptedString = new database(ActivityLogin.this).getTPin();
            String mPin = Encrytion.decrypt(encryptedString);
            if ( (mPin.length() == 4) && enterMode.equals(EnterMode.ENTER_PIN)){
                checkFingerSensor();
            }
        }


        //show message if app was locked due to inactivity
        if (getIntent().getBooleanExtra("session_expired", false)) {
            Popups.showPopup(this, getString(R.string.session_expired), "");
        }

        fingerPrintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFingerSensor();
            }
        });

        findViewById(R.id.Support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Support support = new Support(ActivityLogin.this);
                support.showSupport();
            }
        });
    }




    public void acceptPressed(View view) {
        switch (enterMode) {
            case ENTER_WALLET_ID:
                btnChangeID.setVisibility(View.VISIBLE);
                enterMode = EnterMode.ENTER_PIN;
                setAcceptButtonEnabled(false);
                btnAccept.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void authorize(String mPin) {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Result> task = new AsyncTask<Void, Void, Result>() {

            @Override
            protected Result doInBackground(Void... params) {
                DeviceInfo deviceInfo = session.getDeviceInfo();
                if (deviceInfo == null) {
                    return new Result(Result.CODE_ERROR, getString(R.string.auth_internal_error));
                }

                Response<AuthResponse> authResponse;
                try {
                    authResponse = appMananagerAPI.auth(
                            walletID,
                            mPin,
                            deviceInfo.getImei(),
                            deviceInfo.getAndroidID(),
                            deviceInfo.getSimNumber(),
                            deviceInfo.getLocationCoordinates(),
                            BuildConfig.VERSION_NAME,
                            BuildConfig.VERSION_CODE
                    ).execute();

                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result(Result.CODE_ERROR, getString(R.string.auth_internal_error));
                }

                if (authResponse.code() != 200) {
                    return new Result(Result.CODE_ERROR, getString(R.string.auth_network_issues));
                }

                AuthResponse authResponseBody = authResponse.body();
                if (authResponseBody == null) {
                    return new Result(Result.CODE_ERROR, getString(R.string.auth_internal_error));
                }

                if (authResponseBody.status != 0) {
                    return new Result(Result.CODE_ERROR, authResponseBody.message);
                }

                session.iopAvailableCuntries = authResponseBody.iopAvailableCuntries;
                session.setDeviceInfo(deviceInfo);
                session.walletId = walletID;
                session.pin = pin;

                String accountType = authResponseBody.accountType;
                switch (accountType) {
                    case "AGENT":
                        session.setAccountType(Session.AccountType.AGENT);
                        session.setAgentName(authResponseBody.agentName);
                        session.setAgentShortCode(authResponseBody.agentShortCode);
                        break;
                    case "CUSTOMER":
                        session.setAccountType(Session.AccountType.CUSTOMER);
                        session.setCustomerFirstName(authResponseBody.firstname);
                        session.setCustomerMiddleName(authResponseBody.middlename);
                        session.setCustomerLastName(authResponseBody.lastname);
                        break;
                    case "AGGREGATOR":
                        session.setAccountType(Session.AccountType.AGGREGATOR);
                        session.setAgentName(authResponseBody.agentName);
                        session.setAgentShortCode(authResponseBody.agentShortCode);
                    case "CORPORATE":
                        session.setAccountType(Session.AccountType.CORPORATE);
                        session.setAgentName(authResponseBody.agentName);
                        session.setAgentShortCode(authResponseBody.agentShortCode);
                }

                session.setContacts(mContacts);
                session.setMenuItems(authResponseBody.menuItemList);
                session.setMainBalance(authResponseBody.mainBalance);
                session.setCommissionBalance(authResponseBody.commissionBalance);
                session.savedBeneficiaries = authResponseBody.savedBeneficiaries;
                session.aesKey = authResponseBody.aesKey;
                session.kycLevel = authResponseBody.kycLevel;

                //do not show advert screen after successful login
                editor.putBoolean("show_advert", false);
                editor.putString("walletID", walletID);
                editor.commit();
                //startNotification();
                return new Result(Result.CODE_SUCCESS);
            }

            @SuppressLint("ResourceAsColor")
            @Override
            protected void onPostExecute(Result result) {
                if (result.code == Result.CODE_SUCCESS) {
                    //textEnterPin.setTextColor(R.color.teasyGreen);
                    Pin.showSuccess();
                    pinView.stopAnim();
                    pinView.setVisibility(View.GONE);
                    Pin.setVisibility(View.VISIBLE);
                    //textEnterPin.setVisibility(View.VISIBLE);
                   // textEnterPin.setText("Success!");
                    String encryptedString = Encrytion.encrypt(pin);
                    String encryptedWallet = Encrytion.encrypt(session.walletId);
                    String encryptedType = Encrytion.encrypt(session.getAccountType().name());
                    String encryptedShortCode = Encrytion.encrypt(session.getAgentShortCode());
                    new database(getApplicationContext()).insertWallet(1,encryptedWallet,encryptedType,encryptedShortCode);
                    new database(getApplicationContext()).insertPin(1,encryptedString);
                    Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                    intent.putExtra("Session", session);
                    startActivity(intent);
                    finish();
                }

                if (result.code == Result.CODE_ERROR) {
                    String title = getString(R.string.auth_error_title);
                    showPopup(result.errorMessage, title);

                    pin = "";
                    //textEnterPin.setText(getString(R.string.login_enter_pin));
                    Pin.setVisibility(View.VISIBLE);
                    Pin.setOTP("");
                    Pin.showError();
                    pinView.stopAnim();
                    pinView.setVisibility(View.GONE);
                    Pin.requestFocusOTP();
                    //textEnterPin.setVisibility(View.VISIBLE);
                }
            }
        };

        task.execute();
    }

    private class Result {
        final static int CODE_SUCCESS = 0;
        final static int CODE_ERROR = 1;

        int code;
        String errorMessage;

        private Result(int code) {
            this.code = code;
            this.errorMessage = "Unsuccessful";
        }

        private Result(int code, String errorMessage) {
            this.code = code;
            this.errorMessage = errorMessage;
        }
    }

    private void showPopup(final String msg, final String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Popups.showPopup(ActivityLogin.this, msg, title);
            }
        });
    }

    public void changeIdPressed(View view) {
        String temp = btnChangeID.getText().toString();
        if(temp.equals("Change ID")) {
            if (pin.length() == PIN_LENGTH) {
                return;
            }
            pin = "";
            walletID = "";
            editor.putString("walletID", "");
            editor.commit();

            //textEnterPin.setText(getString(R.string.login_enter_wallet_id));
            Pin.setVisibility(View.GONE);
            Number.setVisibility(View.VISIBLE);
            Number.setOTP("");
            btnChangeID.setText("Register");
            enterMode = EnterMode.ENTER_WALLET_ID;
            textView.setText("Enter Registered Phone Number (Wallet ID)");
            setAcceptButtonEnabled(false);
            btnAccept.setVisibility(View.VISIBLE);
            Number.requestFocusOTP();
        }else{
            Intent intent = new Intent(ActivityLogin.this,ActivityRegistration.class);
            startActivity(intent);
            finish();
        }
    }

    private void setAcceptButtonEnabled(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button button = (Button) findViewById(R.id.btn_accept);
                button.setEnabled(enabled);

                int backgroundId = enabled ? R.drawable.btn_login_active : R.drawable.btn_login_inactive;
                button.setBackgroundDrawable(getResources().getDrawable(backgroundId));
            }
        });
    }

    private enum EnterMode {
        ENTER_WALLET_ID, ENTER_PIN
    }

    @Override
    public void onBackPressed() {
        if (sharedPref.getBoolean("show_advert", true)) {
            super.onBackPressed();
        } else {
            exitDialog = new Dialog(this);
            exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = getLayoutInflater().inflate(R.layout.popup_exit_app, null);
            exitDialog.setContentView(view);
            exitDialog.show();
        }
    }

    public void exitDialogYesClicked(View view) {
        exitDialog.dismiss();
        ExitActivity.exitApplication(this);
    }

    public void exitDialogNoClicked(View view) {
        exitDialog.dismiss();
    }

    public void supportClicked(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void checkFingerSensor() {
        androidx.biometric.BiometricManager biometricManager =
                androidx.biometric.BiometricManager.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {

                // this means we can use biometric sensor
                case BiometricManager.BIOMETRIC_SUCCESS:
                    fingerPrintbtn.setVisibility(View.VISIBLE);
                    executeBio();
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

    private void executeBio() {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(ActivityLogin.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Pin.showError();
                Pin.setOTP("");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Pin.showError();
                Pin.setOTP("");
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                if(!(new database(ActivityLogin.this).getLEV() < 1)){
                    String encryptedString = new database(ActivityLogin.this).getTPin();
                    String mPin = Encrytion.decrypt(encryptedString);
                    pin = mPin;

                    pinView.setVisibleDots(pin.length());
                    pinView.setVisibility(View.VISIBLE);
                    Pin.setVisibility(View.GONE);
                    //textEnterPin.setVisibility(View.GONE);
                    Pin.setOTP(pin);
                    pinView.startAnim();

                    //authorize(mPin);
                }

            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("TEASYPAY")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        biometricPrompt.authenticate(promptInfo);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                //Log.e(TAG, "onActivityResult: app download failed");
                finish();
            }
        }
    }
}

