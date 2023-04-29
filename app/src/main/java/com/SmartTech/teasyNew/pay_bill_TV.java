package com.SmartTech.teasyNew;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.enter_amount;
import com.SmartTech.teasyNew.activity.transaction.SpinnerReselect;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AEDCGetMeterInfoResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetInterswitchBillersResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetInterswitchPaymentItemsResponse;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.view.AmountInputField;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    pay_bill_TV extends BaseResponse {
     private Context context;
     private String description;
     private MainActivity activity;
    RecyclerView popRecycler;
    private String categories;
    private int itemNo;
    private Session session;
    private boolean aedc;
    private EditText meterNumber,phone_Number;
    Dialog myDialog;
    TextView mContinue;

    private String billername;

    public pay_bill_TV(Context context, String description, MainActivity activity,
                       Session session, int itemNo,String categories, boolean aedc){
        this.context = context;
        this.session = session;
        this.description = description;
        this.activity = activity;
        this.categories = categories;
        this.itemNo = itemNo;
        this.aedc = aedc;
    }

    public Dialog billPayment(){

        myDialog = new Dialog(activity);
        myDialog.setContentView(R.layout.pay_bill_new);
        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().getAttributes().windowAnimations = R.anim.bottom_up;
        popRecycler = myDialog.findViewById(R.id.TVRecycler);
        phone_Number = myDialog.findViewById(R.id.editTextNumber31);
        mContinue = myDialog.findViewById(R.id.textView69);
        CardView spCard = myDialog.findViewById(R.id.spCard);
        CardView phonCard = myDialog.findViewById(R.id.meterCard);
        SpinnerReselect mySP = myDialog.findViewById(R.id.spinnerReselect3);
        mContinue.setVisibility(View.GONE);
        meterNumber = myDialog.findViewById(R.id.editTextNumber3);
        popRecycler.setLayoutManager(new LinearLayoutManager(activity));
        meterNumber.setHint(description);
        spCard.setVisibility(View.GONE);
        phonCard.setVisibility(View.GONE);
        if(categories.equals("ELECTRICITY")){
            spCard.setVisibility(View.VISIBLE);
            phonCard.setVisibility(View.VISIBLE);
        }
        String[] arrayitems ={"Self","Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity, R.layout.my_custom_sp_style, arrayitems);
        adapter.setDropDownViewResource(R.layout.my_custom_sp_style);
        mySP.setAdapter(adapter);

        mySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(mySP.getSelectedItem().equals("Self")){
                    phonCard.setVisibility(View.GONE);
                }else {
                    phonCard.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getBillers(categories);
        return myDialog;
    }

    private void getBillers(String Catigory){

        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Loading biller...");
        Callback<GetInterswitchBillersResponse> callback = new Callback<GetInterswitchBillersResponse>() {
            @Override
            public void onResponse(Call<GetInterswitchBillersResponse> call, Response<GetInterswitchBillersResponse> response) {
                sendRequestDialog.dismiss();

                if (!response.isSuccessful()) {
                    showFailPopup("Transaction failed");
                    return;
                }


                List<GetInterswitchBillersResponse.InterswitchBiller> billersAdapter = new ArrayList<>();
                GetInterswitchBillersResponse responseBody = response.body();

                if(responseBody.getResponseCode() == ResponseCode.OK) {
                    billersAdapter.addAll(responseBody.getBillers());
                    if(aedc){
                        for(int i = 0; i < billersAdapter.size(); ++i){
                            Log.e("biller", "onResponse: " + billersAdapter.get(i).name);
                            if(billersAdapter.get(i).name.equals(billername)){
                                getPaymentItems(billersAdapter.get(i));
                            }
                        }
                    }else {
                        getPaymentItems(billersAdapter.get(itemNo));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetInterswitchBillersResponse> call, Throwable t) {
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

        activity.getAppMananagerAPI()
                .getInterswitchBillers(Catigory)
                .enqueue(callback);

    }

    private void getPaymentItems(GetInterswitchBillersResponse.InterswitchBiller mBiller){

        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Loading billers...");
        Callback<GetInterswitchPaymentItemsResponse> callback = new Callback<GetInterswitchPaymentItemsResponse>() {
            @Override
            public void onResponse(Call<GetInterswitchPaymentItemsResponse> call, Response<GetInterswitchPaymentItemsResponse> response) {
                sendRequestDialog.dismiss();

                if (!response.isSuccessful()) {
                    showFailPopup("Transaction failed");
                    return;
                }

                ArrayList<GetInterswitchPaymentItemsResponse.InterswitchPaymentItem> arrayList = new ArrayList<>();
                GetInterswitchPaymentItemsResponse responseBody = response.body();
                Log.e("biller", "onResponse: ok" + responseBody.getResponseCode());
                if(responseBody.getResponseCode() != ResponseCode.OK) {
                    showFailPopup("Transaction failed");
                    return;
                }
                arrayList.addAll(responseBody.getPaymentItems());
                popRecycler.setLayoutManager(new LinearLayoutManager(activity));
                TVAdapter myAdapter = new TVAdapter(arrayList, new TVAdapter.dataOnclickListener() {
                    @Override
                    public void onDATAlister(int position) {
                        if(meterNumber.getText().toString().trim().equals("") || meterNumber.getText().length() <=6){
                            meterNumber.setError("Invalid Meter Number Entered");
                            return;
                        }
                        final long[] paymentAmount = new long[1];
                        confirm_purchase confirmPurchase = new confirm_purchase(activity);
                        String itemName = arrayList.get(position).paymentitemname;
                        String PaymentCode = arrayList.get(position).paymentCode;
                       long isFixed = arrayList.get(position).amount;
                        confirmPurchase.setCategory(categories);
                        if(isFixed > 0){
                            paymentAmount[0] = arrayList.get(position).amount;
                            confirmPurchase.setBiller(mBiller.name);
                            confirmPurchase.setBillerID(mBiller.billerId);
                            confirmPurchase.setmMeterNO(meterNumber.getText().toString().trim());
                            confirmPurchase.setPayMentCode(PaymentCode);
                            confirmPurchase.setAmount(paymentAmount[0]);
                            confirmPurchase.setItemName(itemName);
                            confirmPurchase.Confirm_Purchase(myDialog);
                        }else{
                            enter_amount qAmount = new enter_amount(activity);
                            qAmount.enterAmount().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(!(qAmount.getmAmount().getValueInCents() >= 50000)){
                                        qAmount.getmAmount().setError("The Minimum Amount is N500");
                                        return;
                                    }
                                    confirmPurchase.setDisplayAmount(qAmount.getmAmount().getText().toString().trim());
                                    paymentAmount[0] = qAmount.getmAmount().getValueInCents();
                                    confirmPurchase.setPhoneNumber(phone_Number.getText().toString().trim());
                                    confirmPurchase.setBiller(mBiller.name);
                                    confirmPurchase.setBillerID(mBiller.billerId);
                                    confirmPurchase.setmMeterNO(meterNumber.getText().toString().trim());
                                    confirmPurchase.setPayMentCode(PaymentCode);
                                    confirmPurchase.setAmount(paymentAmount[0]);
                                    confirmPurchase.setItemName(itemName);
                                    qAmount.getMyDialog().dismiss();
                                    confirmPurchase.Confirm_Purchase(myDialog);



                                }
                            });
                        }





                    }

                }, activity);
                popRecycler.setAdapter(myAdapter);


            }

            @Override
            public void onFailure(Call<GetInterswitchPaymentItemsResponse> call, Throwable t) {
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
        activity.getAppMananagerAPI()
                .getInterswitchPaymentItems(mBiller.billerId)
                .enqueue(callback);

    }

    public void setBillername(String billername) {
        this.billername = billername;
    }
    //0 dstv
    //1 actv
    //2 startimes
    //3 kwewetv
    //4 tstv
    //5 trend tv
    //6 Go Tv



    private void AEDCMeterValidation(String MeterNumber, AmountInputField amount, String RCPhoneNumber){
        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Loading Meter Info...");
        Callback<AEDCGetMeterInfoResponse> callback = new Callback<AEDCGetMeterInfoResponse>() {
            @Override
            public void onResponse(Call<AEDCGetMeterInfoResponse> call, Response<AEDCGetMeterInfoResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup("Transaction failed");
                    return;
                }
                AEDCGetMeterInfoResponse responseBody = response.body();
                confirm_purchase confirm_purchase = new confirm_purchase(activity);
                confirm_purchase.setBiller(responseBody.name +"\n" +
                        MeterNumber +"\n"+ responseBody.address);
                confirm_purchase.setmMeterNO(MeterNumber);
                confirm_purchase.setAmount(amount.getValueInCents());
                confirm_purchase.setName(responseBody.name);
                confirm_purchase.setAddress(responseBody.address);
                confirm_purchase.setPhoneNumber(RCPhoneNumber);
                confirm_purchase.setMeterdetails("₦"+amount.getText().toString());
                confirm_purchase.setDescription("Abuja Electricity Distribution Company");
                confirm_purchase.Confirm_PurchaseAEDC(myDialog);



            }

            @Override
            public void onFailure(Call<AEDCGetMeterInfoResponse> call, Throwable t) {
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
        activity.getAppMananagerAPI()
                .aedcGetMeterInfoRequest(MeterNumber)
                .enqueue(callback);


    }
    public Dialog AEDCPayment(){
        myDialog = new Dialog(activity);
        myDialog.setContentView(R.layout._aedc_interface);
        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().getAttributes().windowAnimations = R.anim.bottom_up;
        AmountInputField amount = myDialog.findViewById(R.id.amountInputField2);
        EditText meterNumber = myDialog.findViewById(R.id.editTextNumber3);
        TextView Continue = myDialog.findViewById(R.id.textView69);
        EditText ReciversPhone = myDialog.findViewById(R.id.PhoneNumber);
        SpinnerReselect spinner = myDialog.findViewById(R.id.spinnerReselect2);
        CardView phone = myDialog.findViewById(R.id.phoneCard);
        String[] arrayitems ={"Self","Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity, R.layout.my_custom_sp_style, arrayitems);
        adapter.setDropDownViewResource(R.layout.my_custom_sp_style);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItem().toString().equals("Self")){
                    phone.setVisibility(View.GONE);
                }else{
                    phone.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getValueInCents() < 50000){
                    amount.setError("Amount Must be above 499 naira");
                    return;
                }
                if(spinner.getSelectedItem().toString().equals("Self")) {
                    if(!meterNumber.getText().toString().trim().equals("")) {
                        AEDCMeterValidation(meterNumber.getText().toString().trim()
                                , amount, session.walletId);
                    }else{
                        meterNumber.setError("You Must Enter Meter Number");
                    }
                }else {
                    if (!ReciversPhone.getText().toString().trim().equals("") &&
                            ReciversPhone.getText().toString().trim().length() == 11 ) {
                        if (!meterNumber.getText().toString().trim().equals("")) {
                            AEDCMeterValidation(meterNumber.getText().toString().trim()
                                    , amount, ReciversPhone.getText().toString().trim());
                        } else {
                            meterNumber.setError("You Must Enter Meter Number");
                        }
                    }else{
                        ReciversPhone.setError("You Must Enter Receivers Phone Number");
                    }
                }
            }
        });


       return myDialog;
    }



}
