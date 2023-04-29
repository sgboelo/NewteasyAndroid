package com.SmartTech.teasyNew;

import static com.SmartTech.teasyNew.Session.AccountType.CUSTOMER;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.database;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetBalanceV2Response;
import com.SmartTech.teasyNew.model.BankTransferModel;
import com.SmartTech.teasyNew.model.Notification;
import com.SmartTech.teasyNew.model.SavedBeneficiary;
import com.codingending.popuplayout.PopupLayout;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TranferCash#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranferCash extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View parentView;
    private Session session;
    MainActivity sesTest;
    Context context;
    ViewGroup mGroup;
    private List<SavedBeneficiary> savedBeneficiaryList = new ArrayList<>();
    TextView textViewMainBalance;
    private List<Notification> cache = new ArrayList<>();
    public TranferCash() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TranferCash.
     */
    // TODO: Rename and change types and number of parameters
    public static TranferCash newInstance(String param1, String param2) {
        TranferCash fragment = new TranferCash();
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
        parentView = inflater.inflate(R.layout.fragment_tranfer_cash, container, false);
        sesTest =(MainActivity) getActivity();
        session =  sesTest.getSession();
        mGroup = container;
        context = getContext();
        CardView walletToBank = parentView.findViewById(R.id.wallet_to_bank);
        CardView walletToWallet = parentView.findViewById(R.id.wallet_to_wallet);
        textViewMainBalance = parentView.findViewById(R.id.textView62);
        CircularProgressBar circularProgressBar = parentView.findViewById(R.id.circularProgressBar);
        circularProgressBar.setRoundBorder(true);
        circularProgressBar.setProgressWithAnimation(100, 1000L);
        walletToBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Bundle bundle = new Bundle();
                BankTransferModel transferModel = new BankTransferModel();
                transferModel.setPreviousFragment("Send Cash");
                bundle.putSerializable("key",transferModel);
                Fragment fragment = new BankTransfer();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();*/

                if(session.getAccountType() == CUSTOMER){
                    Fragment fragment = new BankTransfer();
                    BankTransferModel transferModel = new BankTransferModel();
                    Bundle bundle = new Bundle();
                    transferModel.setPreviousFragment("Send Cash");
                    transferModel.setAccountType("MAIN");
                    bundle.putSerializable("key", transferModel);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView,
                                    fragment).addToBackStack("bankTransfer").commit();
                }else {
                    BankTransferOption();
                }

            }
        });
        onBalanceUpdate();
        walletToWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletTransferOption();
                /*
                TransferToTeasy mTransfer = new TransferToTeasy(getContext(),sesTest,session);
                mTransfer.Transfer();




                 */
            }
        });
        final SwipeRefreshLayout pullToRefresh = parentView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Callback<GetBalanceV2Response> callback = new Callback<GetBalanceV2Response>() {
                            @Override
                            public void onResponse(Call<GetBalanceV2Response> call, Response<GetBalanceV2Response> response) {

                                if(!response.isSuccessful()) {
                                    onFail();
                                    return;
                                }
                                GetBalanceV2Response responseBody = response.body();
                                if(responseBody.getResponseCode() == BaseResponse.ResponseCode.OK) {
                                    String mainBalance = responseBody.mainBalance;
                                    String commissionBalance = responseBody.commissionBalance;
                                    session.setMainBalance(mainBalance);



                                    try {
                                        String formattedMainBalance = Utils.formatBalance(mainBalance);
                                        //String formattedCommissionBalance = Utils.formatBalance(commissionBalance);

                                        textViewMainBalance.setText("₦" + formattedMainBalance );

                                    } catch (NumberFormatException ex) {
                                        textViewMainBalance.setText("Not Available");

                                    }
                                }
                                pullToRefresh.setRefreshing(false);

                            }

                            @Override
                            public void onFailure(Call<GetBalanceV2Response> call, Throwable t) {

                                onFail();
                            }

                            private void onFail() {
                                pullToRefresh.setRefreshing(false);
                            }
                        };

                        sesTest.getAppMananagerAPI().getBalanceV2Request(
                                session.walletId, session.pin,
                                session.getAccountType().toString(),
                                session.getAgentShortCode()
                        ).enqueue(callback);




                    }
                };

                handler.post(runnable);

            }
        });
        return parentView;
    }


    @Override
    public void onResume() {
        super.onResume();

        sesTest =(MainActivity) getActivity();
        onBalanceUpdate();
        if(new database(sesTest).getmStatus() == 1) {
            textViewMainBalance.setTransformationMethod(new PasswordTransformationMethod());
            //textViewCommissionBalance.setTransformationMethod(new PasswordTransformationMethod());
            new database(sesTest).insertToggle(1,1);
        }else{
            textViewMainBalance.setTransformationMethod(null);
            //textViewCommissionBalance.setTransformationMethod(null);
            new database(sesTest).insertToggle(1,0);

        }
    }

    protected void onBalanceUpdate() {
        String mainBalance = session.getMainBalance();
        mainBalance = "₦" + Utils.formatBalance(mainBalance);
        textViewMainBalance.setText(mainBalance);
    }


    private void walletTransferOption(){
        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.wallet_to_wallet_option, mGroup,false);
        LinearLayout QR = view.findViewById(R.id.teasy);
        LinearLayout wallet = view.findViewById(R.id.bank);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
                Bundle bundle = new Bundle();
                BankTransferModel transferModel = new BankTransferModel();
                transferModel.setPreviousFragment("Send Cash");
                bundle.putSerializable("key",transferModel);
                Fragment fragment = new QRcode();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
            }
        });


        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
                Bundle bundle = new Bundle();
                BankTransferModel transferModel = new BankTransferModel();
                transferModel.setPreviousFragment("Send Cash");
                bundle.putSerializable("key",transferModel);
                bundle.putString("qr", "");


                Fragment fragment = new Wallet2Wallet();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
            }
        });
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);
    }

    private  void  BankTransferOption(){
        //1. intializ where popfrom

        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.account_option, mGroup,false);
        //View view = View.inflate(getContext(),R.layout.confirm_layout,null);
        LinearLayout Bank_MMO = view.findViewById(R.id.bank);
        LinearLayout Teasy = view.findViewById(R.id.teasy);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        Fragment fragment = new BankTransfer();
        Bundle bundle = new Bundle();
        BankTransferModel transferModel = new BankTransferModel();
        Bank_MMO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
                transferModel.setAccountType("MAIN");
                bundle.putSerializable("key", transferModel);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).addToBackStack("bankTransfer").commit();

            }
        });

        Teasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
                transferModel.setAccountType("COMMISSION");
                bundle.putSerializable("key", transferModel);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).addToBackStack("bankTransfer").commit();
            }
        });
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);

    }

}