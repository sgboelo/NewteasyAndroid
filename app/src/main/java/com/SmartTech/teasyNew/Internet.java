package com.SmartTech.teasyNew;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SmartTech.teasyNew.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Internet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Internet extends Fragment implements View.OnClickListener {

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
    public Internet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Internet.
     */
    // TODO: Rename and change types and number of parameters
    public static Internet newInstance(String param1, String param2) {
        Internet fragment = new Internet();
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
        parentView = inflater.inflate(R.layout.fragment_internet, container, false);
        activity =(MainActivity) getActivity();
        session =  activity.getSession();
        CardView Spectranet, swift;
        Spectranet =parentView.findViewById(R.id.spectranet);
        swift = parentView.findViewById(R.id.swift);
        Spectranet.setOnClickListener(this);
        swift.setOnClickListener(this);
        return parentView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.spectranet:{
                tvLoad(1);
                break;
            } case R.id.swift:{
                tvLoad(0);
                break;
            }
        }
    }

    private void tvLoad(int position){
        pay_bill_TV bill_tv = new pay_bill_TV(getContext(),
                "Modem Number",
                activity,
                session,
                position,
                "INTERNET",
                false
        );
        bill_tv.billPayment().show();
    }
}