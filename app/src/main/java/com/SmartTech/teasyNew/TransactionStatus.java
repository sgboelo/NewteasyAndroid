package com.SmartTech.teasyNew;

import static com.SmartTech.teasyNew.Session.AccountType.AGENT;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BankTransferRespondsModel;
import com.SmartTech.teasyNew.model.BankTransferModel;
import com.shuhart.stepview.StepView;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionStatus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionStatus extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View parentView;
    MainActivity activity;
    Session session;
    BankTransferRespondsModel bankDetails;

    public TransactionStatus() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionStatus.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionStatus newInstance(String param1, String param2) {
        TransactionStatus fragment = new TransactionStatus();
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
        parentView = inflater.inflate(R.layout.fragment_transaction_status, container, false);
        activity = (MainActivity) getActivity();
        session = activity.getSession();
        String[] strArray = {"TeasyMobile", "NIP", "BANK"};
        List<String> items = Arrays.asList(strArray);
        StepView stepView = parentView.findViewById(R.id.step_view);
        stepView.setSteps(items);
        TextView amount = parentView.findViewById(R.id.textView208);
        TextView H0 = parentView.findViewById(R.id.textView226);
        TextView H1 = parentView.findViewById(R.id.textView227);
        TextView H2 = parentView.findViewById(R.id.textView228);
        TextView H3 = parentView.findViewById(R.id.textView229);
        TextView hStatus = parentView.findViewById(R.id.textView230);
        TextView tv0 = parentView.findViewById(R.id.textView219);
        TextView tv1 = parentView.findViewById(R.id.textView220);
        TextView tv2 = parentView.findViewById(R.id.textView221);
        TextView tv3 = parentView.findViewById(R.id.textView222);
        TextView status = parentView.findViewById(R.id.textView223);
        TextView generate = parentView.findViewById(R.id.textView225);
        ImageButton imageButton = parentView.findViewById(R.id.imageButton14);
        TextView checkStatus = parentView.findViewById(R.id.textView224);
        Bundle bundle = this.getArguments();
        BankTransferModel transferModel = (BankTransferModel) bundle.getSerializable("key");
        String mStatus = bundle.getString("status");
        String amt  = bundle.getString("amount");
        hStatus.setText("Status:");
        status.setText(mStatus);
        H0.setText("Recipients Financial Institution:");
        tv0.setText(transferModel.getBankName());
        H1.setText("Recipient Account Number:");
        tv1.setText(transferModel.getAccNumber());
        H2.setText("Recipient Account Name");
        tv2.setText(transferModel.getAccountName());
        generate.setEnabled(false);
        if(mStatus.equalsIgnoreCase("success")){
            stepView.go(1,true);
            generate.setEnabled(true);
            bankDetails = (BankTransferRespondsModel) bundle.getSerializable("Bank");
        }
        String t = format(amt.trim());
        amount.setText(t);
        tv3.setVisibility(View.GONE);
        H3.setVisibility(View.GONE);
        checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckStatus checkST = new CheckStatus(activity);
                if(stepView.getCurrentStep() == 2){
                    return;
                }
                if(stepView.getCurrentStep() == 1){
                    if(bankDetails.getSessionID() == null){
                        return;
                    }
                    if(bankDetails.getSessionID().equals("")){
                        return;
                    }

                    String sessionID = bankDetails.getSessionID();
                    String bankCode = "100010";
                    checkST.getNIBSSStatus(status,sessionID,stepView,bankCode);

                }else {
                    long amtTemp = Long.parseLong(amt.trim());
                    checkST.getStatus(status, amtTemp, stepView, generate);
                }

            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionResult tranactionSucessful = new TransactionResult(activity,activity);
                tranactionSucessful.successfulTransaction();
                if(transferModel.getBankName() != null) {
                    tranactionSucessful.getBankName().setText(transferModel.getBankName());
                }else{
                    tranactionSucessful.getBankName().setText("");
                }

                if(transferModel.getAccountName() != null) {
                    tranactionSucessful.getName().setText(transferModel.getAccountName());
                }else{
                    tranactionSucessful.getName().setText("");
                }
                if(transferModel.getAccNumber() != null) {
                    tranactionSucessful.getAccountNumber().setText(transferModel.getAccNumber());
                }else{
                    tranactionSucessful.getAccountNumber().setText("");
                }
                tranactionSucessful.getAmount().setText(t);
                tranactionSucessful.getNarration().setText(transferModel.getNarration());
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
                    Fragment fragment = new BankTransfer();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, fragment).commit();

                    return true;
                }
                return false;
            }
        } );

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fragment = new BankTransfer();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment).commit();
            }
        });
        return parentView;
    }

    private String format(String Amount){
        return  "₦" + Utils.formatBalance(Amount);
    }
}