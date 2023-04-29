package com.SmartTech.teasyNew;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.transaction.Airtime_Data;
import com.SmartTech.teasyNew.model.DataAirtimeModel;
import com.SmartTech.teasyNew.view.AmountInputField;
import com.bumptech.glide.Glide;
import com.codingending.popuplayout.PopupLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Make_AIrtem_Data_Purchase#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Make_AIrtem_Data_Purchase extends Fragment {

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
    private ViewGroup mGroup;

    public Make_AIrtem_Data_Purchase() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Make_AIrtem_Data_Purchase.
     */
    // TODO: Rename and change types and number of parameters
    public static Make_AIrtem_Data_Purchase newInstance(String param1, String param2) {
        Make_AIrtem_Data_Purchase fragment = new Make_AIrtem_Data_Purchase();
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
        parentView = inflater.inflate(R.layout.fragment_make__a_irtem__data__purchase, container, false);
        TextView currentAmount = parentView.findViewById(R.id.textView195);
        TextView phoneNumber = parentView.findViewById(R.id.name);
        TextView product = parentView.findViewById(R.id.textView197);
        AmountInputField amount =  parentView.findViewById(R.id.editTextNumberDecimal4);
        ImageView NetworkLogo = parentView.findViewById(R.id.imageView49);
        TextView purchase = parentView.findViewById(R.id.textView16);
        ImageView back = parentView.findViewById(R.id.imageView36);
        activity = (MainActivity) getActivity();
        session = activity.getSession();
        mGroup = container;
        Bundle bundle = this.getArguments();
        DataAirtimeModel dataAirtimeModel = (DataAirtimeModel) bundle.getSerializable("key");
        String type = dataAirtimeModel.getType();
        if(type.equals("airtime")){
            product.setText("Airtime");
        }else{
            amount.setEnabled(false);
            String description = dataAirtimeModel.getData().get("decription");
            String amt = dataAirtimeModel.getData().get("denomination") + "00";
            amount.setText(amt);
            product.setText(description);

        }
        String pNumber = dataAirtimeModel.getData().get("number");
        phoneNumber.setText(pNumber);
        String network = dataAirtimeModel.getNetwork();
        changeImage(NetworkLogo,network);
        String bal = formatBalance(session.getMainBalance());
        currentAmount.setText(bal);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long inputamount = amount.getValueInCents();
                long sessionAmount = Long.parseLong(session.getMainBalance());

                if (sessionAmount < inputamount){
                    amount.setError("You cant recharge below your balance");
                    return;
                }

                if (!(inputamount > 0)){
                    return;
                }
                if(type.equals("airtime")){
                    pAirtime(network, inputamount, pNumber);
                }else{
                    String productID = dataAirtimeModel.getData().get("productID");
                    String fvalue = dataAirtimeModel.getData().get("faceValue");
                    String deno = dataAirtimeModel.getData().get("denomination");
                    String description = dataAirtimeModel.getData().get("decription");
                    ptest(network,productID,fvalue,deno,pNumber,description,bundle);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ManageCard();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
            }
        });
        return parentView;
    }



    private void changeImage(ImageView networkLogo, String network){
       switch (network){
            case "MTN":{
                Glide.with(activity)
                        .load(R.drawable.logo_mtn)
                        .into(networkLogo);
                break;
            }
            case "GLO":{
                Glide.with(activity)
                        .load(R.drawable.logo_glo)
                        .into(networkLogo);
                break;
            }
            case "AIRTEL":{
                Glide.with(activity)
                        .load(R.drawable.logo_airtel)
                        .into(networkLogo);


                break;
            }
            case "9MOBILE":{
                Glide.with(activity)
                        .load(R.drawable.logo_etisalat)
                        .into(networkLogo);
                break;
            }
        }
    }

    private String  formatBalance(String Balance){
        return "₦" + Utils.formatBalance(Balance);
    }

    private  void  ptest(String network, String productID, String faceValue, String denomination, String phoneNumber,String  ProductDesc, Bundle bundle){
        //1. intializ where popfrom
        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.confirm_test, mGroup,false);
        //View view = View.inflate(getContext(),R.layout.confirm_layout,null);
        TextView cancelBTN = view.findViewById(R.id.textView35);
        TextView confirmBTN = view.findViewById(R.id.textView36);
        TextView H0 = view.findViewById(R.id.textView209);
        TextView B0 = view.findViewById(R.id.textView210);
        TextView H2 = view.findViewById(R.id.textView215);
        TextView B2 = view.findViewById(R.id.textView216);
        TextView H1 = view.findViewById(R.id.textView212);
        TextView B1 = view.findViewById(R.id.textView211);
        TextView H3 = view.findViewById(R.id.textView213);
        TextView B3 = view.findViewById(R.id.textView214);
        TextView H4 = view.findViewById(R.id.textView217);
        TextView B4 = view.findViewById(R.id.textView218);
        CheckBox save = view.findViewById(R.id.save);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        H0.setText("Network");
        B0.setText(network);
        String temp = formatBalance(denomination + "00");
        H2.setText("Amount");
        B2.setText(temp);
        H1.setText("Phone Number");
        B1.setText(phoneNumber);
        H3.setText("Data Plan");
        B3.setText(ProductDesc);
        H4.setVisibility(View.GONE);
        B4.setVisibility(View.GONE);
        save.setVisibility(View.GONE);

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
            }
        });

        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Airtime_Data buyAirtime = new Airtime_Data(activity, activity, Gravity.CENTER, mGroup);
               buyAirtime.dataPINRQ(phoneNumber,denomination,productID,faceValue, ProductDesc, network);
                popupLayout.dismiss();
            }
        });
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);

    }


    private  void  pAirtime(String network, long Amount, String phoneNumber){
        //1. intializ where popfrom
        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.confirm_test, mGroup,false);
        //View view = View.inflate(getContext(),R.layout.confirm_layout,null);
        TextView cancelBTN = view.findViewById(R.id.textView35);
        TextView confirmBTN = view.findViewById(R.id.textView36);
        TextView H0 = view.findViewById(R.id.textView209);
        TextView B0 = view.findViewById(R.id.textView210);
        TextView H2 = view.findViewById(R.id.textView215);
        TextView B2 = view.findViewById(R.id.textView216);
        TextView H1 = view.findViewById(R.id.textView212);
        TextView B1 = view.findViewById(R.id.textView211);
        TextView H3 = view.findViewById(R.id.textView213);
        TextView B3 = view.findViewById(R.id.textView214);
        TextView H4 = view.findViewById(R.id.textView217);
        TextView B4 = view.findViewById(R.id.textView218);
        CheckBox save = view.findViewById(R.id.save);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        H0.setText("Network");
        B0.setText(network);
        String temp = formatBalance(String.valueOf(Amount));
        H2.setText("Amount");
        B2.setText(temp);
        H1.setText("Phone Number");
        B1.setText(phoneNumber);
        H3.setText("Type");
        B3.setText("Airtime");
        H4.setVisibility(View.GONE);
        B4.setVisibility(View.GONE);
        save.setVisibility(View.GONE);

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
            }
        });

        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Airtime_Data buyAirtime = new Airtime_Data(activity, activity, Gravity.CENTER, mGroup);
                buyAirtime.airtimePIN(phoneNumber,Amount, network);
                popupLayout.dismiss();
            }
        });
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);

    }

}