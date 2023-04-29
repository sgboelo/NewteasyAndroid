package com.SmartTech.teasyNew;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.activity.DataAdapter;
import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.transaction.Airtime_Data;
import com.SmartTech.teasyNew.activity.transaction.SpinnerReselect;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.PADataValidationResponse;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.popups.PopupOperationInProcess;
import com.SmartTech.teasyNew.view.AmountInputField;
import com.codingending.popuplayout.PopupLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickAirtime  implements DataAdapter.dataOnclickListener{
    private String mproductID, mdinomination, mfaceValue, phoneNumber;
    private TextView mNumber, Network, Value;
    private LinearLayout mlayout;
    private ArrayList<PADataValidationResponse.Product> DataItems = new ArrayList<>();
    private String number;
    private ViewGroup vGroup;
    private PopupLayout popupLayout;
    private MainActivity activity;
    public QuickAirtime(ViewGroup vGroup,MainActivity activity) {
        this.vGroup = vGroup;
        this.activity = activity;
    }

    public void BuyAirtime(){
        LayoutInflater inflater = LayoutInflater.from(vGroup.getContext());
        View view = inflater.inflate(R.layout.quick_airtime_pop, vGroup,false);
        //TextView buyButton = myDialog.findViewById(R.id.textView77);
        SpinnerReselect Select_other = view.findViewById(R.id.rse);
        EditText Number = view.findViewById(R.id.editTextNumber2);
        SpinnerReselect type = view.findViewById(R.id.rechargeTypeSP);
        AmountInputField amount = view.findViewById(R.id.editTextNumberDecimal3);

        TextView buyBtn = view.findViewById(R.id.textView77);
        mlayout  = view.findViewById(R.id.confirm);
        mNumber = view.findViewById(R.id.textView80);
        Network = view.findViewById(R.id.textView82);
        Value = view.findViewById(R.id.textView81);
        mlayout.setVisibility(View.GONE);
        popupLayout = PopupLayout.init(vGroup.getContext(), view);
        String[] self_other_items = {"Buy for Self", "Buy for Other"};
        ArrayAdapter<String> selfadapter = new ArrayAdapter<>(
                activity, R.layout.my_custom_sp_style, self_other_items);
        selfadapter.setDropDownViewResource(R.layout.my_custom_sp_style);
        Select_other.setAdapter(selfadapter);
        //DataTypeCard.setVisibility(View.GONE);
        amount.setGravity(Gravity.CENTER);




        Select_other.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(Select_other.getSelectedItem().toString().equals("Buy for Self")){
                    Number.setVisibility(View.GONE);
                    phoneNumber = activity.getSession().walletId;

                }else {

                    Number.setVisibility(View.VISIBLE);
                    Number.setHint("Enter Receivers Phone Number");
                    Number.setEllipsize(TextUtils.TruncateAt.END);
                }
                mlayout.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] typList = {"Airtime", "Data"};
        ArrayAdapter<String> Type_adapter = new ArrayAdapter<>(
                activity, R.layout.my_custom_sp_style, typList);
        Type_adapter.setDropDownViewResource(R.layout.my_custom_sp_style);
        type.setAdapter(Type_adapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(type.getSelectedItem().toString().equals("Airtime")){
                    amount.setVisibility(View.VISIBLE);

                }else{
                    amount.setVisibility(View.GONE);
                    if (Select_other.getSelectedItem().toString().equals("Buy for Self")){
                        numberValidation(activity.getSession().walletId);
                    }else{
                        if(!Number.getText().toString().trim().equals("")
                                && Number.getText().toString().trim().length() == 11) {
                            numberValidation(Number.getText().toString().trim());
                            phoneNumber = Number.getText().toString().trim();
                        }else{
                            Number.setError("You must enter a Phone number");
                            return;
                        }
                    }


                }
                mlayout.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Airtime_Data buyAirtime = new Airtime_Data(activity, activity, Gravity.CENTER, vGroup);
                if (type.getSelectedItem().toString().equals("Airtime")) {
                    if (Select_other.getSelectedItem().toString().equals("Buy for Self")) {
                        number = activity.getSession().walletId;
                    } else {

                        number = Number.getText().toString();
                        if (Number.getText().toString().equals("") &&
                                Number.getText().toString().length() != 11) {
                            Number.setError("Enter a valid phone number");
                            return;
                        }

                    }
                    long mAmount = amount.getValueInCents();
                    long limit = 50 * 100;
                    if (mAmount < limit) {
                        amount.setError("You cant buy airtime below 50 Naira");
                        return;
                    }


                    buyAirtime.airtimePIN(number, mAmount, "");
                }else{

                    buyAirtime.dataPINRQ(phoneNumber,mdinomination,mproductID,mfaceValue,"", "");
                }

                popupLayout.dismiss();
            }
        });

        amount.setGravity(Gravity.CENTER);
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               mlayout.setVisibility(View.VISIBLE);
                netWorkCheck check = new netWorkCheck();
                Network.setText(check.networkByPhone(phoneNumber));
                mNumber.setText(phoneNumber);
                Value.setText(amount.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mlayout.setVisibility(View.VISIBLE);
                netWorkCheck check = new netWorkCheck();
                Network.setText(check.networkByPhone(phoneNumber));
                mNumber.setText(phoneNumber);
                Value.setText(amount.getText().toString());
            }
        });

        Number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    phoneNumber = Number.getText().toString().trim();
                    netWorkCheck check = new netWorkCheck();
                    Network.setText(check.networkByPhone(phoneNumber));
                    mNumber.setText(phoneNumber);
                    Value.setText(amount.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        popupLayout.show(PopupLayout.POSITION_BOTTOM);

    }
    private  void numberValidation(String number){
        PopupOperationInProcess popupOperationInProcess = new PopupOperationInProcess(activity);
        popupOperationInProcess.setPopupText("Please wait");
        popupOperationInProcess.show();
        DataItems.clear();
        activity.getAppMananagerAPI().paDataValidationRequest(number).enqueue(new Callback<PADataValidationResponse>() {
            @Override
            public void onResponse(Call<PADataValidationResponse> call, Response<PADataValidationResponse> response) {
                popupOperationInProcess.dismiss();
                if(!response.isSuccessful()) {
                    onFail("Unsuccessful");
                    return;
                }

                PADataValidationResponse responseModel = response.body();
                if(responseModel.status != 0) {
                    onFail(responseModel.message);
                    return;
                }
                DataItems.addAll(responseModel.products);
                dataPop(DataItems).show();


            }

            @Override
            public void onFailure(Call<PADataValidationResponse> call, Throwable t) {
                popupOperationInProcess.dismiss();
                onFail("Unsuccessful");
            }
            private void onFail(String text) {
                PopupOperationFailed popup = new PopupOperationFailed(activity);
                popup.setText(text);
                popup.setButtonText("Return");
                popup.setButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });
                popup.show();
            }
        });


    }


    private PopupLayout dataPop(ArrayList<PADataValidationResponse.Product> DataItems){
        LayoutInflater inflater = LayoutInflater.from(vGroup.getContext());
        View view = inflater.inflate( R.layout.data_pop, vGroup,false);

        RecyclerView popRecycler = view.findViewById(R.id.dataRecycler);
        popupLayout = PopupLayout.init(vGroup.getContext(), view);
        popRecycler.setLayoutManager(new LinearLayoutManager(activity));
        DataAdapter myAdapter = new DataAdapter(DataItems,this,activity);
        popRecycler.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        return popupLayout;

    }


    @Override
    public void onDATAlister(String productID, String denomination, String faceValue,
                             String decription) {
        mproductID = productID;
        mdinomination = denomination;
        mfaceValue = faceValue;
        popupLayout.dismiss();
        mNumber.setText(phoneNumber);
        Value.setText(decription);
        netWorkCheck check = new netWorkCheck();
        Network.setText(check.networkByPhone(phoneNumber));
        mlayout.setVisibility(View.VISIBLE);
    }


}
