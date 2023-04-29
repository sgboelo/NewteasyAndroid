package com.SmartTech.teasyNew;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.braintreepayments.cardform.view.CardForm;
import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.CardChargeAmountValidationResponse;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.popups.PopupOperationInProcess;
import com.SmartTech.teasyNew.popups.PopupOperationSuccess;
import com.SmartTech.teasyNew.view.AmountInputField;

import org.json.JSONException;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayWithCard {
    private Context context;
    private int gravity;
    private MainActivity activity;
    public PayWithCard(Context context, int gravity, MainActivity activity) {
        this.context = context;
        this.gravity = gravity;
        this.activity = activity;
    }


    private void paywithBank(){
        Dialog confirmkPIN = new Dialog(context);
        confirmkPIN.setContentView(R.layout._with_card);
        Window window = confirmkPIN.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmkPIN.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView addButton = confirmkPIN.findViewById(R.id.ppp);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardform();
            }
        });

        confirmkPIN.show();
    }

    public void cardform(){

        Dialog confirmk = new Dialog(context);
        confirmk.setContentView(R.layout.card_add);
        Window window = confirmk.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmk.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardForm cardForm =  confirmk.findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(activity);
        TextView Continue = confirmk.findViewById(R.id.textView74);
        ((ImageButton) confirmk.findViewById(R.id.imageButton2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmk.dismiss();
            }
        });
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardForm.getCardNumber().equals("")){
                    cardForm.setCardNumberError("Cant be left empty");
                    return;
                }
                if(cardForm.getExpirationDateEditText().equals("")){
                    cardForm.setExpirationError("Cant be left empty");
                    return;
                }

                if(cardForm.getCvv().equals("")){
                    cardForm.setCvvError("Cant be left empty");
                    return;
                }

                if(cardForm.isValid()){
                    confirmk.dismiss();
                    email (cardForm.getCardNumber(),  cardForm.getExpirationMonth(),
                            cardForm.getExpirationYear(), cardForm.getCvv());
                }else{
                    Popups.showPopup(activity, "Invalid Card details. \n Check and try again","").show();

                }


            }
        });

        confirmk.show();

        //.cardholderName(CardForm.FIELD_REQUIRED)
    }

    private void email ( String cardnumber, String month, String year, String CVV){
        Dialog confirmk = new Dialog(context);
        confirmk.setContentView(R.layout.email_amount);
        Window window = confirmk.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmk.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText _email = confirmk.findViewById(R.id.editTextTextEmailAddress2);
        _email.setHint("Enter Email");
        _email.setGravity(Gravity.CENTER);

        AmountInputField _amount = confirmk.findViewById(R.id.editTextNumberDecimal2);
        _amount.setHint("Amount");
        _amount.setGravity(Gravity.CENTER);

        TextView proceed = confirmk.findViewById(R.id.textView74);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = _email.getText().toString();
                String amount = _amount.getText().toString();

                if(email.equals("") || amount.equals("")){
                    Popups.showPopup(activity,"Please enter email and/or amount","");
                    _email.setError("Enter Email");
                    _amount.setError("Enter Amount");
                    return;
                }

                if(!email.matches(".+@.+")) {
                    Popups.showPopup(activity, "Please enter your email", null);
                    _email.setError("Invalid Email entered");
                    return;
                }

                long lamount = _amount.getValueInCents();
                if(lamount < 500 * 100) {
                    String msg = "Minimal amount is N500.00";
                    Popups.showPopup(activity, msg, null);
                    return;
                }

                PopupOperationInProcess popup = new PopupOperationInProcess(context);
                popup.setPopupText("Please wait");
                popup.show();

                activity.getAppMananagerAPI().cardChargeAmountValidation(activity.getSession().walletId, _amount.getValueInCents()).enqueue(new Callback<CardChargeAmountValidationResponse>() {
                    @Override
                    public void onResponse(Call<CardChargeAmountValidationResponse> call, Response<CardChargeAmountValidationResponse> response) {
                        popup.dismiss();
                        if(response.body() == null) {
                            onFail("Unusccessful");
                            return;
                        }

                        CardChargeAmountValidationResponse responseBody = response.body();
                        if(responseBody.status != 0) {
                            onFail(responseBody.message);
                            return;
                        }
                        confirmk.dismiss();
                        confirmPayment(cardnumber, month, year,
                                CVV, email, String.valueOf(responseBody.getSourceAmount()),
                                responseBody.getRateDescription(),String.valueOf(responseBody.getAmountToCharge()),
                                responseBody.getHash());


                    }

                    @Override
                    public void onFailure(Call<CardChargeAmountValidationResponse> call, Throwable t) {
                        popup.dismiss();
                        onFail("Unsuccessful");
                    }

                    private void onFail(String message) {

                    }
                });



            }
        });
        confirmk.show();
    }


    private void confirmPayment(String cardnumber, String month, String year, String CVV,
                                String _email, String _Amount, String commision, String Tototal, String hash){
        Dialog confirmk = new Dialog(context);
        confirmk.setContentView(R.layout.confirm_payment);
        Window window = confirmk.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmk.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         TextView CardNo, Email, Amount, ConvinientFee, TotalAmount,ConfirmButton;
         CardNo = confirmk.findViewById(R.id.CardNumber);
         Email = confirmk.findViewById(R.id.Email);
         Amount = confirmk.findViewById(R.id.Amount);
         ConvinientFee = confirmk.findViewById(R.id.ConvF);
         TotalAmount = confirmk.findViewById(R.id.Tamount);
         ConfirmButton = confirmk.findViewById(R.id.textView74);
        String CardNOxt = "";
         for(int i =0; i< cardnumber.length() - 4; i++){
             CardNOxt += "*";
         }


         CardNOxt += cardnumber.substring(cardnumber.length()-4);
         CardNo.setText(CardNOxt);
         Email.setText(_email);
         Amount.setText(Utils.formatBalance(_Amount));
         ConvinientFee.setText(commision);
         TotalAmount.setText(Utils.formatBalance(Tototal));


         ConfirmButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 PaystackSdk.initialize(context);
                 PaystackSdk.setPublicKey(AppConfig.getProperties().getProperty("paystack_public_key"));

                 PopupOperationInProcess popup = new PopupOperationInProcess(activity);
                 popup.setPopupText("Please wait");
                 popup.show();

                 Card card = new Card(
                         cardnumber,
                         Integer.valueOf(month),
                         Integer.valueOf(year),
                         CVV
                 );

                 Charge charge = new Charge();
                 charge.setAmount( Integer.valueOf(Tototal));
                 charge.setEmail(_email);

                 try {
                     charge.putMetadata("teasy_wallet", Utils.phoneToInternational(activity.getSession().walletId));
                     charge.putMetadata("source_amount", _Amount);
                     charge.putMetadata("amount_to_charge", Tototal);
                     charge.putMetadata("hash", hash);

                     String accountType = activity.getSession().getAccountType().name();
                     charge.putMetadata("account_type", accountType);
                 } catch (JSONException e) {
                     e.printStackTrace();
                     popup.dismiss();

                     PopupOperationFailed popupFailed = new PopupOperationFailed(context);
                     popupFailed.setText("Unsuccessful");
                     popupFailed.show();
                     return;
                 }
                 charge.setCard(card);

                 PaystackSdk.chargeCard(activity, charge, new Paystack.TransactionCallback() {
                     @Override
                     public void onSuccess(Transaction transaction) {
                         confirmk.dismiss();
                         popup.dismiss();
                         PopupOperationSuccess popupOperationSuccess = new PopupOperationSuccess(context);
                         popupOperationSuccess.setText("Successful");
                         popupOperationSuccess.show();

                     }

                     @Override
                     public void beforeValidate(Transaction transaction) {

                     }

                     @Override
                     public void onError(Throwable error, Transaction transaction) {
                         popup.dismiss();
                         confirmk.dismiss();
                         PopupOperationFailed popupFailed = new PopupOperationFailed(context);
                         popupFailed.setText("Unsuccessful");
                         popupFailed.show();

                     }
                 });
             }
         });


        confirmk.show();

    }

}
