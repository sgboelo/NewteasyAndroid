package com.SmartTech.teasyNew;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.SmartTech.teasyNew.activity.transaction.SpinnerReselect;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayBill#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayBill extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View ParentView;

    public PayBill() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PayBill.
     */
    // TODO: Rename and change types and number of parameters
    public static PayBill newInstance(String param1, String param2) {
        PayBill fragment = new PayBill();
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
        ParentView = inflater.inflate(R.layout.fragment_pay_bill, container, false);
        SpinnerReselect spinnerReselect = ParentView.findViewById(R.id.spinnerReselect);
        String[] bills = {"Cable TV", "Electricity", "Internet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), R.layout.my_custom_sp_style, bills);
        adapter.setDropDownViewResource(R.layout.my_custom_sp_style);
        spinnerReselect.setAdapter(adapter);

        spinnerReselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = null;

                //Fragment fragment2 = new LoadGame();
                switch (i){
                    case 0: {

                        fragment = new TV();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .commit();
                        break;
                    }
                    case 1: {

                        fragment = new Electricity();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .commit();
                        break;
                    }
                    case 2:{
                        fragment = new Internet();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .commit();
                        break;
                    }


                }
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView2,
                                fragment).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return ParentView;
    }
}