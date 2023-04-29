package com.SmartTech.teasyNew;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.model.DataAirtimeModel;
import com.shuhart.stepview.StepView;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AirtimeDataTransactionStatus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AirtimeDataTransactionStatus extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View parentView;
    private MainActivity activity;
    private Session session;
    public AirtimeDataTransactionStatus() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AirtimeDataTransactionStatus.
     */
    // TODO: Rename and change types and number of parameters
    public static AirtimeDataTransactionStatus newInstance(String param1, String param2) {
        AirtimeDataTransactionStatus fragment = new AirtimeDataTransactionStatus();
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
        parentView = inflater.inflate(R.layout.fragment_airtime_data_transaction_status, container, false);
        activity = (MainActivity) getActivity();
        session = activity.getSession();
        String[] strArray = {"TeasyMobile", "Airtime/Data"};
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
        ImageButton imageButton = parentView.findViewById(R.id.imageButton14);
        TextView checkStatus = parentView.findViewById(R.id.textView224);
        Bundle bundle = this.getArguments();
        DataAirtimeModel dataAirtimeModel = (DataAirtimeModel) bundle.getSerializable("key");
        String type  = dataAirtimeModel.getType();
        String amt = "0";
        String success = "";
        if(type.equals("data")){
            String mAmount = dataAirtimeModel.getData().get("denomination");
            String temp = mAmount + "00";
            amt = temp;
            temp = formatBalance(temp);
            amount.setText(temp);
            String network = dataAirtimeModel.getNetwork();
            H0.setText("Network");
            tv0.setText(network);
            H1.setText("Purchase Type");
            tv1.setText("Data");
            H2.setText("Data Bundle");
            String dataBundle = dataAirtimeModel.getData().get("description");
            tv2.setText(dataBundle);
            H3.setText("Receivers Phone Number");
            String phoneNumber = dataAirtimeModel.getData().get("phone");
            tv3.setText(phoneNumber);
            hStatus.setText("Status");
            String dStatus = dataAirtimeModel.getData().get("status");
            success = dStatus;
            status.setText(dStatus);
            if(dStatus.equals("success")) {
                stepView.go(1, true);
                stepView.done(true);
            }

        }else{
            String mAmount = dataAirtimeModel.getData().get("denomination");
            String temp = mAmount;
            amt = temp;
            temp = formatBalance(temp);
            amount.setText(temp);
            String network = dataAirtimeModel.getNetwork();
            H0.setText("Network");
            tv0.setText(network);
            H1.setText("Purchase Type");
            tv1.setText("Airtime");
            H2.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
            H3.setText("Receivers Phone Number");
            String phoneNumber = dataAirtimeModel.getData().get("phone");
            tv3.setText(phoneNumber);
            hStatus.setText("Status");
            String dStatus = dataAirtimeModel.getData().get("status");
            success = dStatus;
            status.setText(dStatus);
            if(dStatus.equals("success")) {
                stepView.go(1, true);
                stepView.done(true);
            }
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ManageCard();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
            }
        });

        String finalAmt = amt;
        checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stepView.getCurrentStep() == 1){
                    return;
                }
                CheckStatus status1 = new CheckStatus(activity);
                long Amount_ = Long.parseLong(finalAmt);
                status1.getStatus(status, Amount_ ,stepView,null);
            }
        });
        return parentView;
    }

    private String  formatBalance(String Balance){
        return "₦" + Utils.formatBalance(Balance);
    }
}