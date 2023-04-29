package com.SmartTech.teasyNew;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.annotation.Lockable;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BankAccountValidationResponse;
import com.SmartTech.teasyNew.model.BankTransferModel;
import com.SmartTech.teasyNew.model.SavedBeneficiary;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BankTransfer#newInstance} factory method to
 * create an instance of this fragment.
 */
@Lockable
public class BankTransfer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View parentView;
    private MainActivity sesTest;
    private TextView BankTV;
    private String BanKID = "", AccountName = "", AccountNumber = "";
    private Session session;
    ArrayList<SavedBeneficiary> savedBeneficiaryList = new ArrayList<>();
    private LinearLayout nameV;
    private TextView vName;
    private BankTransferModel transferModel;


    public BankTransfer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankTransfer.
     */
    // TODO: Rename and change types and number of parameters
    public static BankTransfer newInstance(String param1, String param2) {
        BankTransfer fragment = new BankTransfer();
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
        parentView = inflater.inflate(R.layout.fragment_bank_transfer, container, false);
        Bundle bundle = this.getArguments();
        sesTest =(MainActivity) getActivity();
        session =  sesTest.getSession();

        if(bundle != null) {
            transferModel = (BankTransferModel) bundle.getSerializable("key");

        }else{
            transferModel = new BankTransferModel();
        }
        Fragment fragment;
        EditText accountNo = parentView.findViewById(R.id.editTextNumber4);
        BankTV = parentView.findViewById(R.id.textView195);
        nameV = parentView.findViewById(R.id.displayName);
        vName = parentView.findViewById(R.id.textView197);
        Button ContinueBTN = parentView.findViewById(R.id.button3);

        nameV.setVisibility(View.GONE);

        switch (transferModel.getPreviousFragment().toUpperCase()){
            case "":{
                fragment = new Home();
                transferModel.setPreviousFragment("Home");
                transferModel.setCurrentFragment("BankTransfer");
                transferModel.setSession(session);

                break;
            } case "SELECTBANK":{
                fragment = new Home();
                transferModel = (BankTransferModel) bundle.getSerializable("key");
                AccountNumber = transferModel.getAccNumber();
                accountNo.setText(AccountNumber);
                String BankName = transferModel.getBankName();
                BanKID = transferModel.getBankCode();
                BankTV.setText(BankName);
                nameVerification();
                break;
            } case "SELECTBENEFICIARY":{
                fragment = new Home();
                transferModel = (BankTransferModel) bundle.getSerializable("key");
                AccountNumber = transferModel.getAccNumber();
                accountNo.setText(AccountNumber);
                String BankName = transferModel.getBankName();
                BanKID = transferModel.getBankCode();
                BankTV.setText(BankName);
                AccountName = transferModel.getAccountName();
                nameV.setVisibility(View.VISIBLE);
                vName.setText(AccountName);
                break;
            } default:{
                fragment = new TranferCash();
            }
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSerializable("key", transferModel);
            fragment.setArguments(bundle);
        }

        loadSavedBeneficiaries();
        Fragment finalFragment = fragment;
        parentView.findViewById(R.id.imageView36).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                finalFragment).commit();
            }
        });
        BankTransferModel finalTransferModel = transferModel;
        parentView.findViewById(R.id.Loadben).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mfragment = new SelectBeneficiary();
                Bundle b = new Bundle();
                b.putSerializable("key", finalTransferModel);
                mfragment.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                mfragment).commit();
            }
        });

        accountNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(accountNo.getText().toString().toCharArray().length > 10){
                    accountNo.setText(accountNo.getText().toString().substring(0,10));
                    accountNo.setError("Account Number cant be above 10 digits");

                }
                ContinueBTN.setEnabled(accountNo.getText().toString().toCharArray().length >= 10);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        parentView.findViewById(R.id.SelectBank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accountNo.length() != 10){
                    accountNo.setError("Account Number cant be above/below 10 digits");
                    return;
                }
                Fragment BanksFrag = new SelectBank();
                Bundle b = new Bundle();
                finalTransferModel.setAccNumber(accountNo.getText().toString());
                b.putSerializable("key", finalTransferModel);
                BanksFrag.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                BanksFrag).commit();
            }
        });

        Bundle finalBundle = bundle;
        ContinueBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountNo.length() != 10){
                    accountNo.setError("Invalid Account number enter");
                    return;
                }
                if(BanKID.equals("")){
                    return;
                }
                if (AccountName.equals("")){
                    return;
                }
                Fragment BanksFrag = new MakeBankTranfer();
                BanksFrag.setArguments(finalBundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                BanksFrag).commit();
            }
        });

        View rootView = parentView.getRootView();
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, new Home()).commit();

                    return true;
                }
                return false;
            }
        } );
        return parentView;
    }


    private void loadSavedBeneficiaries() {
        for(SavedBeneficiary beneficiary : session.savedBeneficiaries) {
            savedBeneficiaryList.add(beneficiary);
        }
    }



    private void nameVerification(){
        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(sesTest, "Verifying Account Number Please Wait..");
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
                        AccountName = responseBody.holderName;
                        nameV.setVisibility(View.VISIBLE);
                        vName.setText(AccountName);
                        transferModel.setAccountName(AccountName);
                        break;
                    } default:
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
                PopupOperationFailed popup = new PopupOperationFailed(sesTest);
                if (!StringUtils.isBlank(message)) {
                    popup.setText(message);
                }
                popup.show();
            }
        };

        sesTest.getAppMananagerAPI().bankAccountValidationRequest(
                BanKID,
                AccountNumber
        ).enqueue(callback);

    }


}