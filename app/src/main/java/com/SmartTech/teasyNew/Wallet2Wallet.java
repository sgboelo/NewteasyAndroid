package com.SmartTech.teasyNew;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetUserDataResponse;
import com.SmartTech.teasyNew.model.QRModel;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.view.AmountInputField;
import com.codingending.popuplayout.PopupLayout;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Wallet2Wallet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Wallet2Wallet extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View parentView;
    ViewGroup mGroup;
    Session session;
    MainActivity activity;

    public Wallet2Wallet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Wallet2Wallet.
     */
    // TODO: Rename and change types and number of parameters
    public static Wallet2Wallet newInstance(String param1, String param2) {
        Wallet2Wallet fragment = new Wallet2Wallet();
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
        parentView = inflater.inflate(R.layout.fragment_wallet2_wallet, container, false);

        Bundle bundle = this.getArguments();

        EditText WalletNumberED = parentView.findViewById(R.id.editTextNumber4);
        TextView balance = parentView.findViewById(R.id.textView195);
        TextView customerNAme = parentView.findViewById(R.id.textView197);
        LinearLayout display = parentView.findViewById(R.id.displayName);
        AmountInputField amount = parentView.findViewById(R.id.amountInputField4);
        Button continuebtn = parentView.findViewById(R.id.button3);

        ImageView close = parentView.findViewById(R.id.imageView36);
        mGroup = container;
        activity = (MainActivity) getActivity();
        session = activity.getSession();
        String formatedAmount = format(session.getMainBalance());
        balance.setText(formatedAmount);
        String qrCode = bundle.getString("qr");
        display.setVisibility(View.GONE);



        if(!qrCode.equals("")){
            if(qrCode.charAt(0) == '{') {
                Gson gson = new Gson();
                QRModel model = gson.fromJson(qrCode, QRModel.class);
                WalletNumberED.setText(model.getWalletNumber());
                WalletNumberED.setEnabled(false);
                customerNAme.setText(model.getName());
                display.setVisibility(View.VISIBLE);
            }
        }else{
            WalletNumberED.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if(charSequence.length() == 11){
                        String wallet = WalletNumberED.getText().toString().trim();
                        wallet = "234"+wallet.substring(1);
                        WalletVerification(wallet, customerNAme, display, WalletNumberED);
                    }else{
                        WalletNumberED.setError("Invalid Wallet Number");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new TranferCash();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
            }
        });
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long sendingamount = amount.getValueInCents();
                String WalleNO = WalletNumberED.getText().toString();
                String name = customerNAme.getText().toString();
                if(WalleNO.length() != 11){
                    WalletNumberED.setError("Invalid Wallet Number Entered");
                    return;
                }
                if (sendingamount < 49 ){
                    amount.setError("Cant send bellow 50 Naira");
                    return;
                }

                if(Long.parseLong(session.getMainBalance()) < sendingamount){
                    amount.setError("Cant send balance too low for this transaction");
                    return;

                }
                confirmTransaction(name, WalleNO, sendingamount);

            }
        });
        return parentView;
    }


    private void confirmTransaction(String name, String number, long Amount){
        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.wallettowallet, mGroup,false);
        TextView walletName = view.findViewById(R.id.textView251);
        TextView Number = view.findViewById(R.id.textView253);
        TextView amount = view.findViewById(R.id.textView255);
        TextView cancel = view.findViewById(R.id.textView35);
        TextView confirm =  view.findViewById(R.id.textView36);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        walletName.setText(name);
        Number.setText(number);
        String amt = format(String.valueOf(Amount));
        amount.setText(amt);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
                pinRQ(Amount, number);
            }
        });
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);
    }


    private void pinRQ(long sendingAmount, String walletNumber){
        final String[] test = {""};
        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.pin_interface, mGroup,false);
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


        PopupLayout popupLayout = PopupLayout.init(getContext(), view);

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
                    if(OTP.getOTP().equals(session.pin)) {
                        popupLayout.dismiss();
                        transferFund(walletNumber, String.valueOf(sendingAmount), OTP.getOTP());
                    }else {
                        Popups.showPopup(activity, "You Entered Incorrect PIN.\nCheck your PIN and Try AGAIN",
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

    private String format(String Amount){
        return  "₦" + Utils.formatBalance(Amount);
    }

    private void transferFund(String Phone, String mAmount,String mPin){

        long amount = Long.valueOf(mAmount);

        final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity, "Verifying Name and Balance");
        Callback<BaseResponse> callback = new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                sendRequestDialog.dismiss();
                if (!response.isSuccessful()) {
                    showFailPopup("Validation failed");
                    return;
                }


                BaseResponse responseBody = response.body();
                switch (responseBody.getResponseCode()) {
                    case OK: {
                        TransactionResult tResult = new TransactionResult(getContext(),activity);
                        tResult.successfulTransaction();
                        tResult.getBankName().setText("TeasyMobile");
                        tResult.getAmount().setText("₦"+Utils.formatBalance(mAmount));
                        tResult.getAccountNumber().setText(Phone);
                        tResult.getNarationHeader().setVisibility(View.GONE);
                        tResult.getRescipiantName().setVisibility(View.GONE);
                        tResult.getName().setVisibility(View.GONE);
                        tResult.getNarration().setVisibility(View.GONE);
                        tResult.getAmountHeader().setVisibility(View.GONE);
                        tResult.getTransactionCode().setVisibility(View.GONE);

                        tResult.getDirectionHeader().setVisibility(View.GONE);
                        tResult.getTransactioncodeHeader().setVisibility(View.GONE);
                        tResult.getDirection().setVisibility(View.GONE);
                        tResult.getConfirmkPIN().show();
                        break;
                    }
                    default:
                        showFailPopup(responseBody.message);
                        break;
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                sendRequestDialog.dismiss();
                showFailPopup("Validation failed");
            }

            private void showFailPopup(String message) {
                PopupOperationFailed popup = new PopupOperationFailed(getContext());
                if (!StringUtils.isBlank(message)) {
                    popup.setText(message);
                }
                popup.show();
            }
        };

        activity.getAppMananagerAPI().walletTransferRequest(
                activity.getSession().walletId,
                mPin,
                Phone,
                amount,
                activity.getSession().getAccountType().name(),
                activity.getSession().getAgentShortCode(),
                activity.getSession().getDeviceInfo().getImei(),
                activity.getSession().getDeviceInfo().getAndroidID(),
                activity.getSession().getDeviceInfo().getSimNumber(),
                activity.getSession().getDeviceInfo().getLocationCoordinates()
        ).enqueue(callback);

    }



    private void WalletVerification(String WalletNO, TextView textView, LinearLayout l, EditText t) {
         //String destNumber = WalletNO.replaceAll("[^0-9]","");

        String sourceNumber = Utils.phoneToInternational(session.walletId);
        if(WalletNO.equals(sourceNumber)) {
            t.setError("Transfer to self is not allowed");
            return;
        }

        Dialog sendRequestDialog = Popups.showOperationInProcessDialog(activity,"Validating Receivers Account...");

        Callback<GetUserDataResponse> callback = new Callback<GetUserDataResponse>() {
            @Override
            public void onResponse(Call<GetUserDataResponse> call, Response<GetUserDataResponse> response) {
                sendRequestDialog.dismiss();
                if(!response.isSuccessful()) {
                    showFailPopup("Validation failed");
                    return;
                }

                GetUserDataResponse responseBody = response.body();
                switch (responseBody.getResponseCode()) {
                    case OK:
                        String accountType = responseBody.accountType;
                        String receiverName;

                        switch (accountType) {
                            case "AGENT":
                                receiverName = responseBody.organizationName;
                                break;
                            default:
                                receiverName = responseBody.firstName;

                                String middleName = responseBody.middleName;
                                if (!StringUtils.isBlank(middleName)) {
                                    receiverName += " " + middleName;
                                }

                                receiverName += " " + responseBody.lastName;
                                break;
                        }

                        textView.setText(receiverName.toUpperCase());
                        l.setVisibility(View.VISIBLE);
                        break;

                    case CUSTOMER_NOT_REGISTERED:

                        showFailPopup("Customer Not Registered");
                        break;

                    default:
                        showFailPopup(responseBody.message);
                        break;
                }
            }

            @Override
            public void onFailure(Call<GetUserDataResponse> call, Throwable t) {
                sendRequestDialog.dismiss();
                showFailPopup("Validation failed");
            }

            private void showFailPopup(String message) {
                PopupOperationFailed popup = new PopupOperationFailed(getContext());
                if(!StringUtils.isBlank(message)) {
                    popup.setText(message);
                }
                popup.show();
            }
        };

        activity.getAppMananagerAPI().getUserDataRequest(WalletNO).enqueue(callback);
    }
}