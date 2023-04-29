package com.SmartTech.teasyNew;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.view.CustomSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fundWallet {
    private Context context;
    private int gravity;
    MainActivity mainActivity;
    Session session;
    public fundWallet(Context context, int gravity,Session session, MainActivity mainActivity) {
       this.context =context;
       this.gravity = gravity;
       this.mainActivity = mainActivity;
       this.session = session;
    }


    public void Fwallet(){
        Dialog confirmkDialog = new Dialog(context);
        confirmkDialog.setContentView(R.layout.new_fundwallet);
        Window window = confirmkDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardView voucha = confirmkDialog.findViewById(R.id.voucha);
        CardView bankCard = confirmkDialog.findViewById(R.id.card);
        CardView fundBank = confirmkDialog.findViewById(R.id.bank_deposit);
        ImageButton closeBTN = confirmkDialog.findViewById(R.id.imageButton8);
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmkDialog.dismiss();
            }
        });

        fundBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BankDeposit bankDeposit =  new BankDeposit(mainActivity);
                bankDeposit.bankSelection();
            }
        });
        bankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayWithCard payWithCard = new PayWithCard(context,gravity,mainActivity);
                confirmkDialog.dismiss();
                payWithCard.cardform();
            }
        });
        voucha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmkDialog.dismiss();
                teasyVoucha();
            }
        });

        confirmkDialog.show();
    }


     public void teasyVoucha(){
         Dialog confirmkDialog = new Dialog(context);
         confirmkDialog.setContentView(R.layout._with_teasy_voucha);
         Window window = confirmkDialog.getWindow();
         WindowManager.LayoutParams wlp = window.getAttributes();
         wlp.gravity = gravity;
         wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
         window.setAttributes(wlp);
         confirmkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         CustomSpinner TransferType = confirmkDialog.findViewById(R.id.customSpinner);
         CardView CardPhone = confirmkDialog.findViewById(R.id.tPhone);
         EditText Phone = confirmkDialog.findViewById(R.id.editTextTextPersonNa1me2);
         EditText PIN = confirmkDialog.findViewById(R.id.editTextTextPersonName2);
         TextView Proceed = confirmkDialog.findViewById(R.id.textView73);
         String[] type = {"Self","Others"};
         ArrayAdapter<String> spinnerAdapter = new
                 ArrayAdapter<>(context,android.R.layout.simple_spinner_item, type);
         spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         TransferType.setAdapter(spinnerAdapter);
         ((ImageButton) confirmkDialog.findViewById(R.id.imageButton8)).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 confirmkDialog.dismiss();
             }
         });

          if(TransferType.getSelectedItem().toString().equals("Self")){
              CardPhone.setVisibility(View.GONE);
          }else{
              CardPhone.setVisibility(View.VISIBLE);
          }
          TransferType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  if(TransferType.getSelectedItem().toString().equals("Self")){
                      CardPhone.setVisibility(View.GONE);
                  }else{
                      CardPhone.setVisibility(View.VISIBLE);
                  }
              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {

              }
          });
         Proceed.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(TransferType.getSelectedItem().toString().equals("Self")){
                     OnProceed( session.walletId, PIN.getText().toString(),session.walletId,
                             TransferType.getSelectedItem().toString());
                 }else{
                     OnProceed( session.walletId, PIN.getText().toString(),Phone.getText().toString(),
                             TransferType.getSelectedItem().toString());
                 }
             }
         });
         confirmkDialog.show();
     }


     private void OnProceed( String Phone, String PIN, String RecieversPhone, String Type){

         String sourceWallet = Phone.replaceAll("[^0-9]", "");
         String voucherPin = PIN.replaceAll("[^0-9]", "");
         String destWallet = RecieversPhone.replaceAll("[^0-9]", "");
         String mString = Type;


         final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(mainActivity,
                 "Transaction In Progress...");

         Callback<BaseResponse> callback = new Callback<BaseResponse>(){
             @Override
             public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {



                 BaseResponse responseBody = response.body();
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

                 if(responseBody.getResponseCode() != BaseResponse.ResponseCode.OK) {
                     sendRequestDialog.dismiss();
                     TransactionResult successTransaction = new TransactionResult(mainActivity, mainActivity);
                     successTransaction.successfulTransaction();
                     successTransaction.getAmountHeader().setVisibility(View.GONE);
                     successTransaction.getTransactionCode().setVisibility(View.GONE);
                     successTransaction.getFinancialInstitution().setText(responseBody.message);
                     successTransaction.getBankName().setVisibility(View.GONE);
                     successTransaction.getAmount().setVisibility(View.GONE);
                     successTransaction.getNarration().setVisibility(View.GONE);
                     successTransaction.getName().setVisibility(View.GONE);
                     successTransaction.getRecipianAc().setVisibility(View.GONE);
                     successTransaction.getTransactioncodeHeader().setVisibility(View.GONE);
                     successTransaction.getAccountNumber().setVisibility(View.GONE);
                     successTransaction.getRescipiantName().setVisibility(View.GONE);
                     successTransaction.getNarationHeader().setVisibility(View.GONE);
                     successTransaction.getConfirmkPIN().show();

                 }
             }

             @Override
             public void onFailure(Call<BaseResponse> call, Throwable t) {
                 onFail();
             }



             private void onFail() {
                 TransactionResult faildTransaction = new TransactionResult(mainActivity,mainActivity);
                 faildTransaction.failedTransaction("Failed..");

             }
         };



         mainActivity.getAppMananagerAPI().useTeasyVoucherRequest(
                 sourceWallet,
                 voucherPin,
                 destWallet,
                 mainActivity.getSession().getDeviceInfo().getImei(),
                 mainActivity.getSession().getDeviceInfo().getAndroidID(),
                 mainActivity.getSession().getDeviceInfo().getSimNumber(),
                 mainActivity.getSession().getDeviceInfo().getLocationCoordinates())
                 .enqueue(callback);


     }
}

