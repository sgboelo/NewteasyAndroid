package com.SmartTech.teasyNew;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.activity.DataAdapter;
import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.transaction.SpinnerReselect;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.PADataValidationResponse;
import com.SmartTech.teasyNew.model.DataAirtimeModel;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.popups.PopupOperationInProcess;
import com.codingending.popuplayout.PopupLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageCard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageCard extends Fragment implements DataAdapter.dataOnclickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View parentview;
    private Session session;
    private MainActivity sesTest;
    private ArrayList<PADataValidationResponse.Product> DataItems = new ArrayList<>();
    private String phoneNumber;
    private TextView mNumber, Network, Value;
    private LinearLayout mlayout;
    private EditText number2;
    private DataAirtimeModel model;
    private ViewGroup viewGroup ;
    PopupLayout popupLayout;
    public ManageCard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageCard.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageCard newInstance(String param1, String param2) {
        ManageCard fragment = new ManageCard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        parentview = inflater.inflate(R.layout.fragment_manage_card, container, false);
        sesTest =(MainActivity) getActivity();
        session =  sesTest.getSession();
        SpinnerReselect Select_other = parentview.findViewById(R.id.rse);
        LinearLayout CardNumber = parentview.findViewById(R.id.PhoneLV);
        number2 = parentview.findViewById(R.id.editTextNumber2);
        SpinnerReselect type = parentview.findViewById(R.id.rechargeTypeSP);
        TextView buyBtn = parentview.findViewById(R.id.textView77);
        mlayout  = parentview.findViewById(R.id.confirm);
        mNumber = parentview.findViewById(R.id.textView80);
        Network = parentview.findViewById(R.id.textView82);
        Value = parentview.findViewById(R.id.textView81);
        LinearLayout selectFromContact = parentview.findViewById(R.id.Loadben);
        Bundle bundle = this.getArguments();

        viewGroup = container;
        mlayout.setVisibility(View.GONE);
        String[] self_other_items = {"Buy for Self", "Buy for Other"};
        ArrayAdapter<String> selfadapter = new ArrayAdapter<>(
                getContext(), R.layout.my_custom_sp_style, self_other_items);
        selfadapter.setDropDownViewResource(R.layout.my_custom_sp_style);
        Select_other.setAdapter(selfadapter);
        //DataTypeCard.setVisibility(View.GONE);
        selectFromContact.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                Fragment fragment = new ContactsFragment();
                sesTest.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
            }
        });


        Select_other.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(Select_other.getSelectedItem().toString().equals("Buy for Self")){
                    CardNumber.setVisibility(View.GONE);
                    phoneNumber = session.walletId;

                }else {

                    CardNumber.setVisibility(View.VISIBLE);
                    number2.setHint("Enter Receivers Phone Number");
                    number2.setEllipsize(TextUtils.TruncateAt.END);
                }
                mlayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] typList = {"Airtime", "Data"};
        ArrayAdapter<String> Type_adapter = new ArrayAdapter<>(
                getContext(), R.layout.my_custom_sp_style, typList);
        Type_adapter.setDropDownViewResource(R.layout.my_custom_sp_style);
        type.setAdapter(Type_adapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(type.getSelectedItem().toString().equals("Data")) {
                    if (Select_other.getSelectedItem().toString().equals("Buy for Self")) {
                        numberValidation(session.walletId);
                    } else {
                        if (number2.getText().toString().trim().length() == 11) {
                            numberValidation(number2.getText().toString().trim());
                            phoneNumber = number2.getText().toString().trim();
                        } else {
                            number2.setError("You must enter a Phone number");
                            return;
                        }
                    }
                }
                mlayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String  number = "";
                if (type.getSelectedItem().toString().equals("Airtime")) {
                    if (Select_other.getSelectedItem().toString().equals("Buy for Self")) {
                        number = session.walletId;
                    } else {

                        number = number2.getText().toString();
                        if (number2.getText().toString().equals("") &&
                                number2.getText().toString().length() != 11) {
                            number2.setError("Enter a valid phone number");
                            return;
                        }

                    }

                    DataAirtimeModel _model = new DataAirtimeModel();
                    netWorkCheck check = new netWorkCheck();
                    String network = check.networkByPhone(number);
                    _model.setNetwork(network);
                    _model.setType("airtime");
                    Map<String, String> m = new HashMap<>();
                    m.put("number", number);
                    _model.setData(m);

                    model = _model;
                }else{
                    number = phoneNumber;
                }


                if(number.equals("")){
                    number2.setError("Invalid Phone Number Entered");
                    return;
                }

                Fragment fragment = new Make_AIrtem_Data_Purchase();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("key", model);
                fragment.setArguments(bundle1);
                sesTest.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();

            }
        });



        number2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length() == 11) {
                    phoneNumber = number2.getText().toString().trim();
                    netWorkCheck check = new netWorkCheck();
                    Network.setText(check.networkByPhone(phoneNumber));
                    mNumber.setText(phoneNumber);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if(bundle != null){
            phoneNumber = bundle.getString("number");
            Select_other.setSelection(1);
            number2.setText(phoneNumber);
        }
        return parentview;
    }


    private  void numberValidation(String number){
        PopupOperationInProcess popupOperationInProcess = new PopupOperationInProcess(getContext());
        popupOperationInProcess.setPopupText("Please wait");
        popupOperationInProcess.show();
        DataItems.clear();
        sesTest.getAppMananagerAPI().paDataValidationRequest(number).enqueue(new Callback<PADataValidationResponse>() {
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
                dataPop(DataItems).show(PopupLayout.POSITION_BOTTOM);


            }

            @Override
            public void onFailure(Call<PADataValidationResponse> call, Throwable t) {
                popupOperationInProcess.dismiss();
                onFail("Unsuccessful");
            }

            private void onFail(String text) {
                PopupOperationFailed popup = new PopupOperationFailed(sesTest);
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
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.data_pop, viewGroup,false);
        //View view = View.inflate(getContext(),R.layout.confirm_layout,null);
        RecyclerView popRecycler = view.findViewById(R.id.dataRecycler);
        popupLayout = PopupLayout.init(getContext(), view);
        popRecycler.setLayoutManager(new LinearLayoutManager(sesTest));
        DataAdapter myAdapter = new DataAdapter(DataItems,this,sesTest);
        popRecycler.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

       return popupLayout;

    }


    @Override
    public void onDATAlister(String productID, String denomination, String faceValue,
                             String decription) {
        DataAirtimeModel DataModel = new DataAirtimeModel();
        netWorkCheck check = new netWorkCheck();
        String network = check.networkByPhone(phoneNumber);
        DataModel.setType("data");
        DataModel.setNetwork(network);
        Map<String, String> m = new HashMap<>();
        m.put("productID", productID);
        m.put("denomination", denomination);
        m.put("faceValue",faceValue);
        m.put("decription", decription);
        m.put("number", phoneNumber);
        DataModel.setData(m);
        model = DataModel;
        mNumber.setText(phoneNumber);
        Value.setText(decription);

        Network.setText(network);
        mlayout.setVisibility(View.VISIBLE);
        popupLayout.dismiss();
    }

/*
    private String manageContacts(String PhoneNumber){
                    String Phone1 = PhoneNumber.replaceAll("\\s+","");
                    Phone1 = Phone1.replaceAll("\\*","");
                    Phone1 = Phone1.replaceAll("#","");
                    String PhonNumber;
                    if(Phone1.length() >= 11){
                        if(Phone1.length()>11){
                            PhonNumber = "0"+Phone1.substring(4);
                        }else{
                            PhonNumber = Phone1;
                        }
                    }else{
                        Message.message(getContext(), "Invalid Phone Number Selected");
                      PhonNumber = "";
                    }

        return PhonNumber;
    }

 */

}