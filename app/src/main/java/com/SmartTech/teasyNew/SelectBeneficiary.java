package com.SmartTech.teasyNew;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.beneficiaryAdapter;
import com.SmartTech.teasyNew.model.Bank;
import com.SmartTech.teasyNew.model.BankTransferModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectBeneficiary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectBeneficiary extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View parentView;
    private MainActivity activity;
    private int gravity;
    private beneficiaryAdapter adapter;
    private LoadBen.LoadBenOnclick loadBenOnclick;
    private List<Bank> bankList;
    private Session session;

    public SelectBeneficiary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectBeneficiary.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectBeneficiary newInstance(String param1, String param2) {
        SelectBeneficiary fragment = new SelectBeneficiary();
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
        parentView = inflater.inflate(R.layout.fragment_select_beneficiary, container, false);
        activity = (MainActivity) getActivity();
        Bundle bundle = this.getArguments();
        session =  activity.getSession();
        BankTransferModel transferModel =  (BankTransferModel) bundle.getSerializable("key");
        SearchView searchView = parentView.findViewById(R.id.msearch);
        ImageView  backBtn = parentView.findViewById(R.id.imageView39);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment BanksFrag = new BankTransfer();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                BanksFrag).commit();
            }
        });

        searchView.setIconifiedByDefault(false);
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

        adapter = new beneficiaryAdapter(activity.getSession().savedBeneficiaries, activity, new beneficiaryAdapter.beneficiaryOnClickListener() {
            @Override
            public void onTransactionListener(int position, String BankID, String BenName, String AcountID, String WalletID, String bankName) {

                Fragment BanksFrag = new BankTransfer();
                String acc = "";
                if(AcountID.equals("")){
                    acc = WalletID;
                }else{
                    acc = AcountID;
                }
                transferModel.setBankCode(BankID);
                transferModel.setBankName(bankName);
                transferModel.setAccountName(BenName);
                transferModel.setPreviousFragment("SELECTBENEFICIARY");
                transferModel.setAccNumber(acc);
                bundle.putSerializable("key",transferModel);
                BanksFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                BanksFrag).commit();

            }
        });
        RecyclerView recyclerView = parentView.findViewById(R.id.mRecylerV);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));
        recyclerView.setAdapter(adapter);


        return parentView;
    }


    private String getBankPlus(String code){
        bankList = Bank.byType(Bank.Type.BANK);
        List<Bank> bankList2 = Bank.byType(Bank.Type.MMO);
        bankList.addAll(bankList2);
        String bank = "";
        for(int i=0; i < bankList.size(); i++){
            if(bankList.get(i).getCode().equals(code)){
                bank = bankList.get(i).getDisplayName();
                break;
            }
        }
        return bank;
    }
}