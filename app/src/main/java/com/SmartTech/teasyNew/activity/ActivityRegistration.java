package com.SmartTech.teasyNew.activity;

import static android.util.Base64.URL_SAFE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.activity.base.BaseActivity;
import com.SmartTech.teasyNew.activity.transaction.MakeTransactionRunnable2;
import com.SmartTech.teasyNew.activity.transaction.result.TransactionResultData;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.utils.SerializableRunnable;
import com.google.common.io.Files;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by muddvayne on 25/04/2017.
 */

public class ActivityRegistration extends BaseActivity {

    private Map<String, Object> countries = new HashMap<>();

    private Class nextActivity = ActivityLogin.class;

    private File photoFile;

    private static final int PHOTO_REQUEST_ID = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

       /* countries = new Gson().fromJson(new InputStreamReader(getResources().openRawResource(R.raw.countries)), Map.class);
        CustomSpinner countrySpinner = (CustomSpinner) findViewById(R.id.spinner_country);

        String[] entries = countries.keySet().toArray(new String[countries.keySet().size()]);
        countrySpinner.setEntries(entries);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> country = (Map<String, Object>) countries.get(parent.getSelectedItem().toString());
                Map<String, String> states = (Map<String, String>) country.get("states");

                CustomSpinner stateSpinner = (CustomSpinner) findViewById(R.id.spinner_state);
                stateSpinner.setEntries(states.keySet().toArray(new String[countries.keySet().size()]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        */


        Class nextActivity = (Class) getIntent().getSerializableExtra("NextActivity");
        if(nextActivity != null) {
            this.nextActivity = nextActivity;
        }
    }

    public void createAccountPressed(View view) {
        String firstName = ((EditText)findViewById(R.id.firstname)).getText().toString();

        if(StringUtils.isBlank(firstName)) {
            ((EditText)findViewById(R.id.firstname)).setError("Required");
            return;
        }

        final String lastName = ((EditText)findViewById(R.id.lastname)).getText().toString();
        if(StringUtils.isBlank(lastName)) {
            ((EditText)findViewById(R.id.lastname)).setError("Required");
            return;
        }

        String phoneNumber = ((EditText)findViewById(R.id.phone)).getText().toString();
        if(StringUtils.isBlank(phoneNumber)) {
            ((EditText)findViewById(R.id.phone)).setError("Required");
            return;
        }

        if(!(phoneNumber.trim().length() == 11)){
            ((EditText)findViewById(R.id.phone)).setError("Phone Number Must be 11 Digits");
            return;
        }

        String pin = ((EditText)findViewById(R.id.pin_edittext)).getText().toString();
        String pinConfirm = ((EditText)findViewById(R.id.pin_confirm_edittext)).getText().toString();

        if(StringUtils.isBlank(pin)) {
            ((EditText)findViewById(R.id.pin_edittext)).setError("Required");
            return;
        }

        if(StringUtils.isBlank(pinConfirm)) {
            ((EditText)findViewById(R.id.pin_confirm_edittext)).setError("Required");
            return;
        }

        if(pin.length() != 4) {
            ((EditText)findViewById(R.id.pin_edittext)).setError("PIN must be 4 digits length");
            return;
        }

        if(!pin.equals(pinConfirm)) {
            ((EditText)findViewById(R.id.pin_confirm_edittext)).setError("Entered PIN doesn't match");
            return;
        }




        String BVN = ((EditText)findViewById(R.id.pin_edittext2)).getText().toString();//
        if(StringUtils.isAllBlank(BVN)){
            ((EditText)findViewById(R.id.pin_edittext2)).setError("BVN is Required");
            return;
        }
        if(!(BVN.trim().length() == 11)){
            ((EditText)findViewById(R.id.pin_edittext2)).setError("Invalid BVN Entered");
            return;
        }

        String photoBase64 = null;
        if (photoFile != null) {
            try {
                byte[] photoBytes = Files.toByteArray(photoFile);
                photoBase64 = Base64.encodeToString(photoBytes, URL_SAFE);
            } catch (IOException ex) {
                PopupOperationFailed popup = new PopupOperationFailed(this);

                String message = "Failed to encode photo: " + ex.getMessage();
                popup.setText(message);
                popup.setButtonText("OK");
                popup.show();
                return;
            }
        }
        RegistrationRunnable runnable = new RegistrationRunnable(phoneNumber, pin, firstName, "", lastName, photoBase64, nextActivity);
        runnable.setTargetObject(this);
        runnable.run();
    }

    @Override
    public boolean onSupportNavigateUp(){
        back();
        return true;
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void onTakePhotoPressed(View view) {
        Intent intent = new Intent(this, ActivityPhoto.class);
        startActivityForResult(intent, PHOTO_REQUEST_ID);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_ID && resultCode == RESULT_OK) {
            photoFile = new File(data.getStringExtra(ActivityPhoto.PHOTO_FILE_PATH_NAME));
            Bitmap preview = data.getParcelableExtra(ActivityPhoto.PREVIEW_NAME);
           // ((Button)findViewById(R.id.takePhotoButton)).setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(), preview), null, null, null);
        }
    }

    private void back() {
        Intent intent = new Intent(this, ActivityLogin.class);
        intent.putExtra("Session", session);
        startActivity(intent);
        finish();
    }

    private static class RegistrationRunnable extends MakeTransactionRunnable2<BaseResponse> {

        private String phoneNumber, pin, firstName, middlename, lastName, photoBase64;

        private Class nextActivity;

        public RegistrationRunnable(String phoneNumber, String pin,
                                    String firstName, String middlename,
                                    String lastName, String phoneBase64, Class nextActivity) {

            this.phoneNumber = phoneNumber;
            this.pin = pin;
            this.firstName = firstName;
            this.middlename = middlename;
            this.lastName = lastName;
            this.nextActivity = nextActivity;
            this.photoBase64 = phoneBase64;
        }

        @Override
        protected Call<BaseResponse> getRequest() {
            return activity.getAppMananagerAPI().registerCustomerRequest(
                    phoneNumber,
                    pin,
                    firstName,
                    middlename,
                    lastName,
                    photoBase64
            );
        }

        @Override
        public SerializableRunnable transactionRetryClicked() {
            return new RegistrationRunnable(phoneNumber, pin, firstName, middlename, lastName, photoBase64, nextActivity);
        }


        @Override
        protected void onSuccess(Response<BaseResponse> response, Intent intent, TransactionResultData transactionResultData) {
            transactionResultData.setPreviousActivityClass(nextActivity);
            transactionResultData.setErrorHeader("Registration successful");
            transactionResultData.setActivityTitle("Registration successful");
            if(nextActivity == ActivityLogin.class) {
                transactionResultData.setButtonText("Proceed to login screen");
            }
            transactionResultData.setButtonOnClickListener(new SerializableRunnable() {
                @Override
                public void run() {
                    super.run();
                    BaseActivity activity = (BaseActivity) this.getTargetObject();
                    Intent intent1 = new Intent(activity, nextActivity);
                    intent1.putExtra("Session", activity.getSession());
                    activity.startActivity(intent1);
                    activity.finish();
                }
            });

            super.onSuccess(response, intent, transactionResultData);
        }

        @Override
        protected void onErrorCode(Response<BaseResponse> response, Intent intent, TransactionResultData transactionResultData) {
            transactionResultData.setErrorHeader("Registration unsuccessful");
            transactionResultData.setActivityTitle("Registration unsuccessful");
            super.onErrorCode(response, intent, transactionResultData);
        }

        @Override
        protected void onFail(Intent intent, TransactionResultData transactionResultData) {
            transactionResultData.setErrorHeader("Registration unsuccessful");
            transactionResultData.setActivityTitle("Registration unsuccessful");
            super.onFail(intent, transactionResultData);
        }
    }
}
