package com.SmartTech.teasyNew;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Switch;

import com.SmartTech.teasyNew.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Electricity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Electricity extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MainActivity activity;
    Session session;
    private View parentView;
    Dialog myDialog;
    private String strName;
    public Electricity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Electricity.
     */
    // TODO: Rename and change types and number of parameters
    public static Electricity newInstance(String param1, String param2) {
        Electricity fragment = new Electricity();
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
        parentView = inflater.inflate(R.layout.fragment_electricity, container, false);
        activity =(MainActivity) getActivity();
        session =  activity.getSession();
        CardView AEDC, YEDC,PHED,KEDCO,IBEDC,EKEDC,EEDC,BEDC;
        AEDC = parentView.findViewById(R.id.AEDC);
        YEDC = parentView.findViewById(R.id.YEDC);
        PHED = parentView.findViewById(R.id.PHED);
        KEDCO = parentView.findViewById(R.id.KEDCO);
        IBEDC = parentView.findViewById(R.id.IBEDC);
        EKEDC = parentView.findViewById(R.id.EKEDC);
        EEDC = parentView.findViewById(R.id.EEDC);
        BEDC = parentView.findViewById(R.id.BEDC);
        AEDC.setOnClickListener(this);
        YEDC.setOnClickListener(this);
        PHED.setOnClickListener(this);
        KEDCO.setOnClickListener(this);
        IBEDC.setOnClickListener(this);
        EKEDC.setOnClickListener(this);
        EEDC.setOnClickListener(this);
        BEDC.setOnClickListener(this);

        return parentView;
    }

    private String PostPaid(){
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout._postpaid_prepaid_option);
        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        CardView prePaid = myDialog.findViewById(R.id.prepaid);
        CardView postPaid = myDialog.findViewById(R.id.postpaid);
        String[] SelectedItem = {"PREPAID"};
        postPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (strName){
                    case "KEDCO":{
                        tvLoad("KANO Electricity Postpaid");
                        myDialog.dismiss();
                        break;
                    } case "EKEDC":{
                        tvLoad("Eko Electric Postpaid");
                        myDialog.dismiss();
                        break;
                    } case "EEDC":{
                        tvLoad("Enugu Electricity Postpaid");
                        myDialog.dismiss();
                        break;
                    } case "BEDC":{
                        tvLoad("Benin Electricity Postpaid");
                        myDialog.dismiss();
                        break;
                    }
                }

            }
        });
        prePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (strName){
                    case "KEDCO":{
                        tvLoad("Kano Electricity Prepaid");
                        myDialog.dismiss();
                        break;
                    } case "EKEDC":{
                        tvLoad("Eko Electric Prepaid");
                        myDialog.dismiss();
                        break;
                    } case "EEDC":{
                        tvLoad("Enugu Electricity Prepaid");
                        myDialog.dismiss();
                        break;
                    } case "BEDC":{
                        tvLoad("Benin Electricity Prepaid");
                        myDialog.dismiss();
                        break;
                    }
                }
            }
        });

        myDialog.show();
        return SelectedItem[0];
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.AEDC:{
                pay_bill_TV bill_tv = new pay_bill_TV(getContext(),
                        "Enter Meter Number",
                        activity,
                        session,
                        0,
                        "ELECTRICITY",
                        true
                );

                bill_tv.AEDCPayment().show();
                break;
            }
            case R.id.YEDC:{
                tvLoad("Yola Electricity");
                break;
            }
            case R.id.PHED:{
                tvLoad("Port Hacourt Electricity");
                break;
            }
            case R.id.KEDCO:{
                strName = "KEDCO";
                PostPaid();

                break;
            }
            case R.id.IBEDC:{
                tvLoad("Ibadan Electricity Prepaid");
                break;
            }
            case R.id.EKEDC:{
                strName = "EKEDC";
                PostPaid();
                break;
            }
            case R.id.EEDC:{
                strName = "EEDC";
                PostPaid();

                break;
            }
            case R.id.BEDC:{
                strName = "BEDC";
                PostPaid();

                break;
            }
        }
    }

    private void tvLoad(String billerName){
        pay_bill_TV bill_tv = new pay_bill_TV(getContext(),
                "Enter Meter Number",
                activity,
                session,
                0,
                "ELECTRICITY",
                true
        );
        bill_tv.setBillername(billerName);
        bill_tv.billPayment().show();
    }
}