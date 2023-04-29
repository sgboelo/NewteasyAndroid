package com.SmartTech.teasyNew;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;
import static com.SmartTech.teasyNew.Session.AccountType.AGENT;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.beneficiaryAdapter;
import com.SmartTech.teasyNew.activity.database;
import com.SmartTech.teasyNew.activity.transaction.SpinnerReselect;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BankAccountValidationResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BankTransferRespondsModel;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.model.Bank;
import com.SmartTech.teasyNew.model.SavedBeneficiary;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.view.AmountInputField;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferTobank {
    private Context context;
    private MainActivity activity;
    private Session session;
    private beneficiaryAdapter adapter;
    private List<SavedBeneficiary> savedBeneficiaryList;
    private int gravity;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private EditText accountNumber, Narration;
    private AmountInputField Amount;
    private SpinnerReselect Select_Bank;
    private final String[] code = new String[1];
    private TextView Verify_RV_Name, tranferFund, Header;
    private List<Bank> bankList;
    Bank.Type SelectType;
    public TransferTobank(MainActivity activity, Context context, Session session,
                          List<SavedBeneficiary> savedBeneficiaryList, int gravity) {
        this.session = session;
        this.activity = activity;
        this.context = context;
        this.savedBeneficiaryList = savedBeneficiaryList;
        this.gravity = gravity;
    }

    public void quickSend (){

        SelectType = Bank.Type.BANK;
        bankList = Bank.byType(SelectType);
        loadSavedBeneficiaries(SelectType);
        Dialog quickDialog = new Dialog(activity);
        quickDialog.setContentView(R.layout.quick_tf_pop);
        Window window = quickDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        quickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        List<String> bankName = new ArrayList<>();
        if(!bankList.isEmpty()){
            for(int i = 0; i < bankList.size(); ++i){
                bankName.add(bankList.get(i).getDisplayName());
            }
        }
        ImageButton closeBtn = quickDialog.findViewById(R.id.imageButton8);
        tranferFund = quickDialog.findViewById(R.id.textView27);
        Header = quickDialog.findViewById(R.id.textView26);
        accountNumber = quickDialog.findViewById(R.id.editTextTextPersonName);
        Amount = quickDialog.findViewById(R.id.editTextNumberDecimal);
        Narration = quickDialog.findViewById(R.id.editTextTextMultiLine2); //editTextNumberDecimal
        Select_Bank = quickDialog.findViewById(R.id.spinner);
        ImageButton loadBen  = quickDialog.findViewById(R.id.imageButton7);
        Verify_RV_Name = quickDialog.findViewById(R.id.textView24);
        Header.setText("Wallet To Bank");
        ArrayAdapter<String> spinnerAdapter = new
                ArrayAdapter<>(context,android.R.layout.simple_spinner_item, bankName);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Select_Bank.setAdapter(spinnerAdapter);
        Select_Bank.setEnabled(false);
        Amount.setEnabled(false);
        Narration.setEnabled(false);
        Verify_RV_Name.setText("");


        tranferFund.setEnabled(false);
        accountNumber.addTextChangedListener(new TextWatcher() {
            //String accNo = accountNumber.getText().toString();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Select_Bank.setEnabled(accountNumber.getText().toString().toCharArray().length >= 10);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Select_Bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> adapterView, View view, int i, long l) {
                if(Select_Bank.isEnabled()) {
                    final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Verifying Account Number Please Wait..");
                    Callback<BankAccountValidationResponse> callback = new Callback<BankAccountValidationResponse>() {
                        @Override
                        public void onResponse(Call<BankAccountValidationResponse> call, Response<BankAccountValidationResponse> response) {
                            sendRequestDialog.dismiss();
                            if (!response.isSuccessful()) {
                                showFailPopup("Validation failed");
                                return;
                            }


                            BankAccountValidationResponse responseBody = response.body();
                            switch (responseBody.getResponseCode()) {
                                case OK: {
                                    tranferFund.setEnabled(true);
                                    Verify_RV_Name.setText(responseBody.holderName);
                                    Amount.setEnabled(true);
                                    Narration.setEnabled(true);
                                    break;
                                }default:
                                    showFailPopup(responseBody.message);
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<BankAccountValidationResponse> call, Throwable t) {
                            sendRequestDialog.dismiss();
                            showFailPopup("Validation failed");
                        }

                        private void showFailPopup(String message) {
                            PopupOperationFailed popup = new PopupOperationFailed(context);
                            if (!StringUtils.isBlank(message)) {
                                tranferFund.setEnabled(false);
                                popup.setText(message);
                            }
                            popup.show();
                        }
                    };

                    //
                    //String selectedName = bankList.get(i).getDisplayName();
                    String SelectedCode = bankList.get(i).getCode();
                    String AccNumber = accountNumber.getText().toString();
                    code[0] = bankList.get(i).getCode();
                    activity.getAppMananagerAPI().bankAccountValidationRequest(
                            SelectedCode,
                            AccNumber
                    ).enqueue(callback);



                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickDialog.dismiss();
            }
        });


        tranferFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sp = Select_Bank.getSelectedItem().toString();
                confirmTransaction(accountNumber.getText().toString(),
                        Verify_RV_Name.getText().toString(), sp, Amount.getText().toString(),
                        code[0],Narration.getText().toString(), activity);
                quickDialog.dismiss();

            }
        });

        loadBen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBen();

            }
        });
        quickDialog.show();
    }

    private void loadSavedBeneficiaries(Bank.Type bank) {
        for(SavedBeneficiary beneficiary : session.savedBeneficiaries) {
            if(bank == Bank.Type.BANK && beneficiary.bankID.startsWith("0")) {
                savedBeneficiaryList.add(beneficiary);

            }

        }
    }

    private void confirmTransaction(String AccountNumber, String AccountName, String BankName, String Amount,
                                    String code, String Narration, MainActivity teasyMain){
        Dialog confirmkDialog = new Dialog(context);
        confirmkDialog.setContentView(R.layout.confirm_transaction_pop);
        Window window = confirmkDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView AccNumber, AccName, BnkName, Amt, cancel, confirmed, naration;
        BnkName = confirmkDialog.findViewById(R.id.textView31);
        AccName =confirmkDialog.findViewById(R.id.textView32);
        AccNumber = confirmkDialog.findViewById(R.id.textView33);
        Amt = confirmkDialog.findViewById(R.id.textView34);
        cancel = confirmkDialog.findViewById(R.id.textView35);
        confirmed = confirmkDialog.findViewById(R.id.textView36);
        naration = confirmkDialog.findViewById(R.id.textView67);
        ImageButton closebtn = confirmkDialog.findViewById(R.id.imageButton9);//imageButton8
        CheckBox SaveBeneficiary = confirmkDialog.findViewById(R.id.save);
        BnkName.setText(BankName);
        AccName.setText(AccountName);
        AccNumber.setText(AccountNumber);
        Amt.setText(Amount);
        naration.setText(Narration);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmkDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmkDialog.dismiss();
            }
        });

        confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean save = SaveBeneficiary.isChecked();
                confirmationPin(save,code,AccountNumber,Amount,teasyMain, BankName,AccountName,Narration);
                confirmkDialog.dismiss();
            }
        });
        confirmkDialog.show();
    }



    private void confirmationPin(boolean checked, String BankID, String AcountNo, String Amount,
                                 MainActivity teasyMain, String BankName, String Namet, String Narration){
        OtpTextView pin;
        TextView Ok;
        ImageView fingerprintBTN;
        Dialog confirmkPIN = new Dialog(context);
        confirmkPIN.setContentView(R.layout.pin_request_pop);
        Window window = confirmkPIN.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmkPIN.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pin = confirmkPIN.findViewById(R.id.firstPinView);
        Ok = confirmkPIN.findViewById(R.id.textView38);
        fingerprintBTN = confirmkPIN.findViewById(R.id.imageView20);
        checkFingerSensor(fingerprintBTN,checked, BankID,AcountNo, Amount,
                 teasyMain,BankName, Namet, Narration, confirmkPIN);
        fingerprintBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeBio(checked, BankID, AcountNo, Amount,
                        teasyMain, BankName, Namet, Narration, confirmkPIN);
            }
        });
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mPin = pin.getOTP();

                //
                if((!mPin.equals(""))&& (mPin.toCharArray().length == 4)) {
                    walletToBank(checked, BankID, Long.parseLong(Amount.replaceAll("[.,]", "")),
                            AcountNo, teasyMain,mPin, BankName, Namet, Narration);

                    confirmkPIN.dismiss();
                }else{
                    pin.showError();
                }
            }
        });

        confirmkPIN.show();
    }


    private void walletToBank(boolean saveBeneficiary, String bankID, long Amount,
                             String AccountNo, MainActivity teasyMain,
                             String PIN, String BankName,String mName, String Narration) {

        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity,
                "Transaction In Progress...");

        Callback<BankTransferRespondsModel> callback = new Callback<BankTransferRespondsModel>(){
            @Override
            public void onResponse(Call<BankTransferRespondsModel> call, Response<BankTransferRespondsModel> response) {



                BankTransferRespondsModel responseBody = response.body();
                if(!response.isSuccessful() || responseBody == null) {
                    sendRequestDialog.dismiss();
                    onFail();
                    return;
                }

                if(responseBody.getResponseCode() != BaseResponse.ResponseCode.OK) {
                    sendRequestDialog.dismiss();
                    onFail();
                    return;
                }

                if(responseBody.getResponseCode() == BaseResponse.ResponseCode.OK) {
                    sendRequestDialog.dismiss();
                    String formattedMainBalance = Utils.formatBalance(Amount);
                    TransactionResult tranactionSucessful = new TransactionResult(teasyMain,teasyMain);
                    tranactionSucessful.successfulTransaction();
                    if(!BankName.equals(null)) {
                        tranactionSucessful.getBankName().setText(BankName);
                    }else{
                        tranactionSucessful.getBankName().setText("");
                    }

                    if(!mName.equals(null)) {
                        tranactionSucessful.getName().setText(mName);
                    }else{
                        tranactionSucessful.getName().setText("");
                    }
                    if(!AccountNo.equals(null)) {
                        tranactionSucessful.getAccountNumber().setText(AccountNo);
                    }else{
                        tranactionSucessful.getAccountNumber().setText("");
                    }

                    tranactionSucessful.getAmount().setText("₦"+formattedMainBalance);
                    if(!Narration.equals(null)) {
                        tranactionSucessful.getNarration().setText(Narration);
                    }else{
                        tranactionSucessful.getNarration().setText("");
                    }
                    String senderName;
                    if(!(session.getAccountType() == AGENT)) {

                        senderName = session.getCustomerFirstName().toUpperCase() + " " + session.getCustomerLastName().toUpperCase();
                    }else{
                        senderName = session.getAgentName();
                    }
                    tranactionSucessful.getDirection().setText("Debit");
                    tranactionSucessful.getTransactioncodeHeader().setText("Senders Name");
                    tranactionSucessful.getTransactionCode().setText(senderName);
                    tranactionSucessful.getConfirmkPIN().show();



                }else{
                    sendRequestDialog.dismiss();
                    onFail();
                }
            }

            @Override
            public void onFailure(Call<BankTransferRespondsModel> call, Throwable t) {
                onFail();
            }



            private void onFail() {
                TransactionResult faildTransaction = new TransactionResult(teasyMain,teasyMain);
                faildTransaction.failedTransaction("Failed..");

            }
        };

        teasyMain.getAppMananagerAPI().bankTransferRequest(
                teasyMain.getSession().walletId,
                PIN,
                bankID,
                AccountNo,
                Amount,
                teasyMain.getSession().getAccountType().name(),
                teasyMain.getSession().getAgentShortCode(),
                "MAIN",
                saveBeneficiary,
                Narration,
                teasyMain.getSession().getDeviceInfo().getImei(),
                teasyMain.getSession().getDeviceInfo().getAndroidID(),
                teasyMain.getSession().getDeviceInfo().getSimNumber(),
                teasyMain.getSession().getDeviceInfo().getLocationCoordinates()).enqueue(callback);
    }




    //"COMMISSION"

    public void toMMO(){
        SelectType = Bank.Type.MMO;
        List<Bank> bankList;
        bankList = Bank.byType(SelectType);
        loadSavedBeneficiaries(SelectType);
        Dialog quickDialog = new Dialog(context);
        quickDialog.setContentView(R.layout.quick_tf_pop);
        Window window = quickDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        quickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        List<String> bankName = new ArrayList<>();
        if(!bankList.isEmpty()){
            for(int i = 0; i < bankList.size(); ++i){
                bankName.add(bankList.get(i).getDisplayName());
            }
        }

        ImageButton closeBtn = quickDialog.findViewById(R.id.imageButton8); //selectBen()
        tranferFund = quickDialog.findViewById(R.id.textView27);
        accountNumber = quickDialog.findViewById(R.id.editTextTextPersonName);
        Amount = quickDialog.findViewById(R.id.editTextNumberDecimal);
        Narration = quickDialog.findViewById(R.id.editTextTextMultiLine2);
        Select_Bank = quickDialog.findViewById(R.id.spinner);
        Verify_RV_Name = quickDialog.findViewById(R.id.textView24);
        Header = quickDialog.findViewById(R.id.textView26);
        Header.setText("Wallet To MMO");
        ((EditText) quickDialog.findViewById(R.id.editTextTextPersonName)).setHint("Enter Wallet Number");
        ((TextView) quickDialog.findViewById(R.id.textView21)).setText("Select MMO");
        ArrayAdapter<String> spinnerAdapter = new
                ArrayAdapter<>(context,android.R.layout.simple_spinner_item, bankName);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Select_Bank.setAdapter(spinnerAdapter);
        Select_Bank.setEnabled(false);
        Amount.setEnabled(false);
        Narration.setEnabled(false);
        Verify_RV_Name.setText("");
        ImageButton loadBen  = quickDialog.findViewById(R.id.imageButton7);

        tranferFund.setEnabled(false);
        accountNumber.addTextChangedListener(new TextWatcher() {
            //String accNo = accountNumber.getText().toString();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(accountNumber.getText().toString().toCharArray().length >= 10){
                    Select_Bank.setEnabled(true);
                }else{
                    Select_Bank.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Select_Bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> adapterView, View view, int i, long l) {
                if(Select_Bank.isEnabled()) {
                    final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Verifying Account Number Please Wait..");
                    Callback<BankAccountValidationResponse> callback = new Callback<BankAccountValidationResponse>() {
                        @Override
                        public void onResponse(Call<BankAccountValidationResponse> call, Response<BankAccountValidationResponse> response) {
                            sendRequestDialog.dismiss();
                            if (!response.isSuccessful()) {
                                showFailPopup("Validation failed");
                                return;
                            }


                            BankAccountValidationResponse responseBody = response.body();
                            switch (responseBody.getResponseCode()) {
                                case OK: {
                                    tranferFund.setEnabled(true);
                                    Verify_RV_Name.setText(responseBody.holderName);
                                    Amount.setEnabled(true);
                                    Narration.setEnabled(true);
                                    break;
                                }default:
                                    showFailPopup(responseBody.message);
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<BankAccountValidationResponse> call, Throwable t) {
                            sendRequestDialog.dismiss();
                            showFailPopup("Validation failed");
                        }

                        private void showFailPopup(String message) {
                            PopupOperationFailed popup = new PopupOperationFailed(context);
                            if (!StringUtils.isBlank(message)) {
                                tranferFund.setEnabled(false);
                                popup.setText(message);
                            }
                            popup.show();
                        }
                    };

                    String SelectedCode = bankList.get(i).getCode();
                    String AccNumber = accountNumber.getText().toString();

                    code[0] = bankList.get(i).getCode();
                    activity.getAppMananagerAPI().bankAccountValidationRequest(
                            SelectedCode,
                            AccNumber
                    ).enqueue(callback);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickDialog.dismiss();
            }
        });


        tranferFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amount.setEnabled(true);
                String sp = Select_Bank.getSelectedItem().toString();

                confirmTransaction(accountNumber.getText().toString(),
                        Verify_RV_Name.getText().toString(), sp, Amount.getText().toString(),
                        code[0],Narration.getText().toString(), activity);
                quickDialog.dismiss();

            }
        });
        loadBen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBen();

            }
        });
        quickDialog.show();

    }

    private void selectBen(){

        Dialog quickDialog = new Dialog(context);
        quickDialog.setContentView(R.layout.select_beneficiary);
        Window window = quickDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        quickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Loading Beneficiary...");
        /*
        adapter = new beneficiaryAdapter(savedBeneficiaryList, context, new beneficiaryAdapter.beneficiaryOnClickListener() {
            @Override
            public void onTransactionListener(int position, String BankID, String BenName, String AcountID, String WalletID) {
                if (SelectType == Bank.Type.BANK) {
                    accountNumber.setText(AcountID);
                }else{
                    accountNumber.setText(WalletID);
                }

                    final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Verifying Account Number Please Wait..");
                    Callback<BankAccountValidationResponse> callback = new Callback<BankAccountValidationResponse>() {
                        @Override
                        public void onResponse(Call<BankAccountValidationResponse> call, Response<BankAccountValidationResponse> response) {
                            sendRequestDialog.dismiss();
                            if (!response.isSuccessful()) {
                                showFailPopup("Validation failed");
                                return;
                            }


                            BankAccountValidationResponse responseBody = response.body();
                            switch (responseBody.getResponseCode()) {
                                case OK: {
                                    tranferFund.setEnabled(true);
                                    Verify_RV_Name.setText(responseBody.holderName);
                                    Amount.setEnabled(true);
                                    Narration.setEnabled(true);
                                    Select_Bank.setEnabled(false);
                                    for (int i = 0; i < bankList.size(); i++) {
                                        if (bankList.get(i).getCode().equals(BankID)) {
                                            Select_Bank.setSelection(i);
                                            break;
                                        }

                                    }
                                    break;
                                }
                                default:
                                    showFailPopup(responseBody.message);
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<BankAccountValidationResponse> call, Throwable t) {
                            sendRequestDialog.dismiss();
                            showFailPopup("Validation failed");
                        }

                        private void showFailPopup(String message) {
                            PopupOperationFailed popup = new PopupOperationFailed(context);
                            if (!StringUtils.isBlank(message)) {
                                tranferFund.setEnabled(false);
                                popup.setText(message);
                            }
                            popup.show();
                        }
                    };

                    //
                    //String selectedName = bankList.get(i).getDisplayName();
                    String AccNumber = accountNumber.getText().toString();

                    code[0] = BankID;
                    activity.getAppMananagerAPI().bankAccountValidationRequest(
                            BankID,
                            AccNumber
                    ).enqueue(callback);

                    quickDialog.dismiss();
            }
        });
        RecyclerView recyclerView = quickDialog.findViewById(R.id.savrRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        TextView textView = quickDialog.findViewById(R.id.textView72);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(!(adapter.getItemCount() == 0)){
            textView.setVisibility(View.INVISIBLE);
        }
        sendRequestDialog.dismiss();
*/
        quickDialog.show();


    }

    private void checkFingerSensor(ImageView fingerPrintbtn, boolean checked, String BankID, String AcountNo, String Amount,
                                   MainActivity teasyMain, String BankName, String Namet, String Narration, Dialog dialog) {
        androidx.biometric.BiometricManager biometricManager =
                androidx.biometric.BiometricManager.from(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {

                // this means we can use biometric sensor
                case BiometricManager.BIOMETRIC_SUCCESS:
                    fingerPrintbtn.setVisibility(View.VISIBLE);
                    executeBio(checked,BankID,AcountNo,Amount,teasyMain,BankName,Namet,Narration,dialog);
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

    private void executeBio(boolean checked, String BankID, String AcountNo, String Amount,
                            MainActivity teasyMain, String BankName, String Namet, String Narration, Dialog dialog) {
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
                    walletToBank(checked, BankID, Long.parseLong(Amount.replaceAll("[.,]", "")),
                            AcountNo, teasyMain,mPin, BankName, Namet, Narration);
                    dialog.dismiss();
                    //authorize(mPin);
                }

            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("TEASYPAY")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        biometricPrompt.authenticate(promptInfo);
    }
}
