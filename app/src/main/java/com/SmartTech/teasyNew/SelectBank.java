package com.SmartTech.teasyNew;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.model.Bank;
import com.SmartTech.teasyNew.model.BankTransferModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectBank#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectBank extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View parentView;
    BankAdapter adapter;
    private MainActivity sesTest;
    Session session;
    public SelectBank() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectBank.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectBank newInstance(String param1, String param2) {
        SelectBank fragment = new SelectBank();
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

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_select_bank, container, false);
        Bundle bundle = this.getArguments();
        sesTest =(MainActivity) getActivity();
        assert sesTest != null;
        session =  sesTest.getSession();
        BankTransferModel transferModel =  (BankTransferModel) bundle.getSerializable("key");
        SearchView searchView = parentView.findViewById(R.id.msearch);
        searchView.setIconifiedByDefault(false);
        parentView.findViewById(R.id.imageView39).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment BanksFrag = new BankTransfer();
                BanksFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                BanksFrag).commit();
            }
        });
        adapter = new BankAdapter(getBanks(), new BankAdapter.OnBanks() {
            @Override
            public void BanksOnClickListener(int position, String BankCode, String BankName) {
                Fragment BanksFrag = new BankTransfer();
                transferModel.setBankCode(BankCode);
                transferModel.setBankName(BankName);
                transferModel.setPreviousFragment("SELECTBANK");
                bundle.putSerializable("key",transferModel);
                BanksFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                BanksFrag).commit();
            }
        },sesTest);
        RecyclerView recyclerView = parentView.findViewById(R.id.mRecylerV);
        recyclerView.setLayoutManager(new LinearLayoutManager(sesTest.getApplicationContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*
        adapter.filterList(mS);
         */
        searchView.setFocusedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String mS = s.toLowerCase();
                adapter.filterList(mS);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String mS= s.toLowerCase();
                adapter.filterList(mS);
                return false;
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




    private List<Bank> getBanks(){
        List<Bank> mBank = Bank.byType(Bank.Type.BANK);
        mBank.addAll(Bank.byType(Bank.Type.MMO));
        return mBank;
    }

}