package com.SmartTech.teasyNew;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.SmartTech.teasyNew.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TV#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TV extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View parentView;
    MainActivity activity;
    Session session;
    public TV() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TV.
     */
    // TODO: Rename and change types and number of parameters
    public static TV newInstance(String param1, String param2) {
        TV fragment = new TV();
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
        parentView = inflater.inflate(R.layout.fragment_t_v, container, false);
        CardView DSTV, ACTV, Startimes, kweseTV, TSTV, TrendTV, GoTV;
        activity =(MainActivity) getActivity();
        session =  activity.getSession();
        DSTV = parentView.findViewById(R.id.DSTV);
        ACTV = parentView.findViewById(R.id.ACTV);
        Startimes = parentView.findViewById(R.id.Startimes);
        kweseTV = parentView.findViewById(R.id.Kwese);
        TSTV = parentView.findViewById(R.id.TStv);
        TrendTV = parentView.findViewById(R.id.TrendTV);
        GoTV = parentView.findViewById(R.id.GoTV);


        DSTV.setOnClickListener(this);
        ACTV.setOnClickListener(this);
        Startimes.setOnClickListener(this);
        kweseTV.setOnClickListener(this);
        TSTV.setOnClickListener(this);
        TrendTV.setOnClickListener(this);
        GoTV.setOnClickListener(this);
        return parentView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.DSTV:{
                tvLoad(0);
                break;
            } case R.id.ACTV:{
                tvLoad(1);
                break;
            } case R.id.Startimes:{
                tvLoad(2);
                break;
            } case R.id.Kwese:{
                tvLoad(3);
              break;
            } case R.id.TStv:{
                tvLoad(4);
                break;
            } case R.id.TrendTV:{
                tvLoad(5);
                break;
            } case R.id.GoTV:{
                tvLoad(6);
            }

        }
    }

    private void tvLoad(int position){
        pay_bill_TV bill_tv = new pay_bill_TV(getContext(),
                "Smart Card Number",
                activity,
                session,
                position,
                "CABLE_TV",
                false
        );
        bill_tv.billPayment().show();
    }
}