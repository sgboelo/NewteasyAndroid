package com.SmartTech.teasyNew;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BankTransferRespondsModel;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.model.BankTransferModel;
import com.SmartTech.teasyNew.view.AmountInputField;
import com.codingending.popuplayout.PopupLayout;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeBankTranfer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeBankTranfer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View parentView;
    private MainActivity sesTest;
    private Session session;
    private String Amount = "", bankname = "", accountname = "", accountNumber = "", BankID;
    private ViewGroup mGroup;
    public MakeBankTranfer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeBankTranfer.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeBankTranfer newInstance(String param1, String param2) {
        MakeBankTranfer fragment = new MakeBankTranfer();
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
        parentView = inflater.inflate(R.layout.fragment_make_bank_tranfer, container, false);
        TextView BankName = parentView.findViewById(R.id.name);
        TextView holderName = parentView.findViewById(R.id.textView197);
        TextView currentBal = parentView.findViewById(R.id.textView195);
        AmountInputField amount = parentView.findViewById(R.id.amountInputField);
        Button transferbtn = parentView.findViewById(R.id.button3);
        ImageView backbtn = parentView.findViewById(R.id.imageView36);
        EditText narration = parentView.findViewById(R.id.editTextTextPersonName4);
        mGroup = container;
        Bundle bundle = this.getArguments();
        sesTest = (MainActivity) getActivity();
        session =  sesTest.getSession();

        BankTransferModel transferModel = (BankTransferModel) bundle.getSerializable("key");
        String bal ;
        long balance;
        if(transferModel.getAccountType().equals("MAIN")) {
            bal = format(session.getMainBalance());
            balance = Long.parseLong(session.getMainBalance());
        }else{
            bal = format(session.getCommissionBalance());
            balance = Long.parseLong(session.getCommissionBalance());
        }
        currentBal.setText(bal);
        bankname = transferModel.getBankName();
        BankName.setText(bankname);
        accountname = transferModel.getAccountName();
        holderName.setText(accountname);
        accountNumber = transferModel.getAccNumber();
        BankID = transferModel.getBankCode();
        transferbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getValueInCents() < 50){
                    amount.setError("Amount must be above N50");
                    return;
                }


                if(!(amount.getValueInCents() <= balance)){
                    amount.setError("Balance too low to make this transaction");
                    return;
                }
                Amount = String.valueOf(amount.getValueInCents());
                String mNarration = narration.getText().toString();
                String type = transferModel.getAccountType();
                ptest(bankname,BankID,accountname,Amount,accountNumber,mNarration, bundle, type);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment BanksFrag = new BankTransfer();

                BanksFrag.setArguments(bundle);
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
                            .replace(R.id.fragmentContainerView, new BankTransfer()).commit();

                    return true;
                }
                return false;
            }
        } );
        return parentView;
    }


    private String format(String Amount){
        return  "₦" + Utils.formatBalance(Amount);
    }


    private  void  ptest(String Bank, String code, String name, String amount, String accNum,String  Naration, Bundle bundle, String type){
        //1. intializ where popfrom
        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.confirm_test, mGroup,false);
        //View view = View.inflate(getContext(),R.layout.confirm_layout,null);
        TextView cancelBTN = view.findViewById(R.id.textView35);
        TextView confirmBTN = view.findViewById(R.id.textView36);
        TextView bank = view.findViewById(R.id.textView210);
        TextView _amount = view.findViewById(R.id.textView216);
        TextView _name = view.findViewById(R.id.textView211);
        TextView accno = view.findViewById(R.id.textView214);
        TextView narration = view.findViewById(R.id.textView218);
        CheckBox save = view.findViewById(R.id.save);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        bank.setText(Bank);
        _amount.setText(format(amount));
        _name.setText(name);
        accno.setText(accNum);
        narration.setText(Naration);

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
            }
        });

        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSave = save.isChecked();
                pinRQ(isSave,code,Long.parseLong(amount),accNum, Naration, bundle, type);
                popupLayout.dismiss();
            }
        });
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);

    }


    @SuppressLint("ClickableViewAccessibility")
    private void pinRQ(boolean saveBen, String BankCode, long sendingAmount, String AccountNumber, String Narration, Bundle bundle, String Type){
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
        Bundle mBundle = bundle;
        BankTransferModel transferModel = (BankTransferModel) bundle.getSerializable("key");
        transferModel.setNarration(Narration);
        mBundle.putSerializable("key", transferModel);
        OTP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

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
        OTP.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                if(otp.equals(session.pin)) {

                    makeW2BTransaction(saveBen, BankCode, sendingAmount,
                            AccountNumber, sesTest,
                            session.pin, Narration, mBundle, Type);

                }else{
                    Popups.showPopup(sesTest, "You Entered Incorrect PIN.\nCheck your PIN and Try AGAIN",
                            "Incorrect PIN Entered");
                }

                popupLayout.dismiss();
            }
        });
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);
    }


    private void makeW2BTransaction(boolean saveBeneficiary, String bankID, long Amount,
                                    String AccountNo, MainActivity teasyMain,
                                    String PIN, String Narration, Bundle bundle, String accountType) {

            final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(teasyMain,
                    "Processing Transaction...");

            Callback<BankTransferRespondsModel> callback = new Callback<BankTransferRespondsModel>() {
                @Override
                public void onResponse(Call<BankTransferRespondsModel> call, Response<BankTransferRespondsModel> response) {

                    BankTransferRespondsModel responseBody = response.body();
                    if(!response.isSuccessful()){
                       sendRequestDialog.dismiss();
                       bundle.putString("status", "Failed");
                       bundle.putString("amount", String.valueOf(Amount));
                       onFail();
                       return;
                    }
                    if(responseBody == null) {
                        sendRequestDialog.dismiss();
                        bundle.putString("status", "Failed");
                        bundle.putString("amount", String.valueOf(Amount));
                        onFail();
                        return;
                    }

                    if(responseBody.getResponseCode() != BaseResponse.ResponseCode.OK) {
                        sendRequestDialog.dismiss();
                        bundle.putString("status", "Pending");
                        bundle.putString("amount", String.valueOf(Amount));
                        onFail();
                        return;
                    }

                    if(responseBody.getResponseCode() == BaseResponse.ResponseCode.OK) {
                        sendRequestDialog.dismiss();
                        bundle.putString("status", "Success");
                        bundle.putString("amount", String.valueOf(Amount));
                        bundle.putSerializable("Bank", responseBody);
                        fragmentT(bundle);
                    }

                }
                @Override
                public void onFailure(Call<BankTransferRespondsModel> call, Throwable t) {
                    sendRequestDialog.dismiss();
                    bundle.putString("status", "Pending");
                    bundle.putString("amount", String.valueOf(Amount));
                    onFail();
                }


                private void onFail() {
                    fragmentT(bundle);
                }
            };

            teasyMain.getAppMananagerAPI().bankTransferRequest(
                    teasyMain.getSession().walletId,
                    PIN.trim(),
                    bankID.trim(),
                    AccountNo.trim(),
                    Amount,
                    teasyMain.getSession().getAccountType().name(),
                    teasyMain.getSession().getAgentShortCode(),
                            accountType,
                    saveBeneficiary,
                    Narration,
                    teasyMain.getSession().getDeviceInfo().getImei(),
                    teasyMain.getSession().getDeviceInfo().getAndroidID(),
                    teasyMain.getSession().getDeviceInfo().getSimNumber(),
                    teasyMain.getSession().getDeviceInfo().getLocationCoordinates())
                    .enqueue(callback);

    }


    private void fragmentT(Bundle bundle){
        Fragment BanksFrag = new TransactionStatus();

        BanksFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView,
                        BanksFrag).commit();
    }

}