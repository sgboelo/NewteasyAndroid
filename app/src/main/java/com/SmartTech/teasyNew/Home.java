package com.SmartTech.teasyNew;

import static com.SmartTech.teasyNew.Session.AccountType.CUSTOMER;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.SmartTech.teasyNew.activity.ActivityLogin;
import com.SmartTech.teasyNew.activity.ActivityRegistration;
import com.SmartTech.teasyNew.activity.ExitApp;
import com.SmartTech.teasyNew.activity.MainActivity;
import com.SmartTech.teasyNew.activity.NotificationAdapter;
import com.SmartTech.teasyNew.activity.annotation.Lockable;
import com.SmartTech.teasyNew.activity.database;
import com.SmartTech.teasyNew.activity.historyAdapter;
import com.SmartTech.teasyNew.activity.transaction.MakeTransactionRunnable2;
import com.SmartTech.teasyNew.activity.transaction.result.TransactionResultData;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetBalanceV2Response;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetNotificationsResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetUserDataResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.TransactionHistoryResponse;
import com.SmartTech.teasyNew.model.Bank;
import com.SmartTech.teasyNew.model.BankTransferModel;
import com.SmartTech.teasyNew.model.ContactModel;
import com.SmartTech.teasyNew.model.Notification;
import com.SmartTech.teasyNew.model.QRModel;
import com.SmartTech.teasyNew.model.historyBankTransferModel;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.utils.SerializableRunnable;
import com.SmartTech.teasyNew.view.CustomTextView;
import com.bumptech.glide.Glide;
import com.codingending.popuplayout.PopupLayout;
import com.google.gson.Gson;
import com.google.zxing.WriterException;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
@Lockable
public class Home extends Fragment implements View.OnClickListener, Serializable {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public interface SessionTransfer{
         void sessionTransferListener(Session s);
    }
    //my variables
    View parentView;
    Session session;
    private historyAdapter mAdapter;
    CircleImageView profileImage;
    CardView frsc, NIs,aedc, dstv, gotv, transactionH;
    LinearLayout Send, Receive;
    MainActivity sesTest;
    private ImageView hideAccount;
    TextView textViewMainBalance,textViewCommissionBalance, number, kycT;
    ViewGroup mGroup;
    private List<TransactionHistoryResponse.TransactionHistoryEntry> cache = new ArrayList<>();
    private List<Notification> notificationCache = new ArrayList<>();
    SessionTransfer sessionTransfer;
    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        parentView = inflater.inflate(R.layout.fragment_home, container, false);
        sesTest =(MainActivity) getActivity();
        //lastTransactionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        session =  sesTest.getSession();

        NIs = parentView.findViewById(R.id.NisCard);
        gotv = parentView.findViewById(R.id.GotvCard);
        aedc = parentView.findViewById(R.id.AedcCard);
        mGroup = container;
        dstv = parentView.findViewById(R.id.DstvCard);
        number = parentView.findViewById(R.id.textView189);
        profileImage = parentView.findViewById(R.id.profile_image);
        Send = parentView.findViewById(R.id.send);
        Receive = parentView.findViewById(R.id.recieve);
        hideAccount = parentView.findViewById(R.id.imageView18);
        textViewMainBalance = parentView.findViewById(R.id.textView4);
        textViewCommissionBalance = parentView.findViewById(R.id.textView15);
        frsc = parentView.findViewById(R.id.FrscCard);
        transactionH = parentView.findViewById(R.id.transactions);
        kycT = parentView.findViewById(R.id.textView257);
        final SwipeRefreshLayout pullToRefresh = parentView.findViewById(R.id.pullToRefresh);
        number.setText(session.walletId);
        String kyc = "Tier: " + String.valueOf(session.kycLevel);
        kycT.setText(kyc);
        sessionTransfer = (SessionTransfer) getActivity();

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
                                    sessionTransfer.sessionTransferListener(session);

                                    try {
                                        String formattedMainBalance = Utils.formatBalance(mainBalance);
                                        String formattedCommissionBalance = Utils.formatBalance(commissionBalance);

                                        textViewMainBalance.setText("₦" + formattedMainBalance );
                                        if(session.getAccountType() == Session.AccountType.AGENT) {
                                            textViewCommissionBalance.setText("Commission: ₦" + formattedCommissionBalance);
                                            textViewCommissionBalance.setVisibility(View.VISIBLE);
                                            session.setCommissionBalance(mainBalance);
                                        }
                                        else {
                                            textViewCommissionBalance.setVisibility(View.GONE);
                                        }
                                    } catch (NumberFormatException ex) {
                                        textViewMainBalance.setText("Not Available");
                                        textViewCommissionBalance.setText("Not Available");
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

        onBalanceUpdate();

        NIs.setOnClickListener(this);
        gotv.setOnClickListener(this);
        aedc.setOnClickListener(this);
        transactionH.setOnClickListener(this);
        dstv.setOnClickListener(this);
        Send.setOnClickListener(this);
        frsc.setOnClickListener(this);
        Receive.setOnClickListener(this);
       // transaction(sesTest);



        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Profile_pop();
            }
        });


        hideAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(new database(sesTest).getmStatus() == 0) {
                    textViewMainBalance.setTransformationMethod(new PasswordTransformationMethod());
                    textViewCommissionBalance.setTransformationMethod(new PasswordTransformationMethod());
                    new database(sesTest).insertToggle(1,1);
                }else{
                    textViewMainBalance.setTransformationMethod(null);
                    textViewCommissionBalance.setTransformationMethod(null);
                    new database(sesTest).insertToggle(1,0);
                }


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
            textViewCommissionBalance.setTransformationMethod(new PasswordTransformationMethod());
            new database(sesTest).insertToggle(1,1);
        }else{
            textViewMainBalance.setTransformationMethod(null);
            textViewCommissionBalance.setTransformationMethod(null);
            new database(sesTest).insertToggle(1,0);

        }
    }


    protected void onBalanceUpdate() {

        if(session.getAccountType() == CUSTOMER){
            try {
                String mainBalance = session.getMainBalance();
                String fBalance = "₦" + Utils.formatBalance(mainBalance);
                textViewMainBalance.setText(fBalance);
                textViewCommissionBalance.setVisibility(View.GONE);
            }catch (Exception ignored){

            }

        }else{
            try {
                String mainBalance = session.getMainBalance();
                String commissionBalance = session.getCommissionBalance();
                String fBalance = "₦" + Utils.formatBalance(mainBalance);
                String fCommission = "Commission: ₦" + Utils.formatBalance(commissionBalance);
                textViewMainBalance.setText(fBalance);
                textViewCommissionBalance.setText(fCommission);
                textViewCommissionBalance.setVisibility(View.VISIBLE);
            }catch (Exception ignored){

            }

        }
        String displayName = getDisplayName();

        TextView DisplayName = parentView.findViewById(R.id.textView18);
        DisplayName.setText(displayName.toUpperCase());
    }

    private String getDisplayName() {
        switch (session.getAccountType()) {
            case CUSTOMER:
                String customerFirstName = session.getCustomerFirstName();
                String customerMiddleName = session.getCustomerMiddleName();
                String customerLastName = session.getCustomerLastName();

                String fullName = customerFirstName;
                if(customerMiddleName != null && !"null".equals(customerMiddleName)) {
                    fullName += " " + customerMiddleName;
                }
                fullName += " " + customerLastName;
                return fullName;

            default:
                return session.getAgentName().replaceAll("[0-9]{6} - ", "");
        }

    }

    private void transaction(MainActivity teasyMain){
        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.history_pop, mGroup,false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar2);
        RecyclerView popRecycler = view.findViewById(R.id.mrecycler);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        popRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar.setVisibility(View.VISIBLE);
        //final Dialog sendRequestDialog = Popups.showOperationInProcessDialog(getActivity(), "Loading Transaction History...");
        try {
            Callback<TransactionHistoryResponse> callback = new Callback<TransactionHistoryResponse>() {
                @Override
                public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {


                    TransactionHistoryResponse responseBody = response.body();
                    if (!response.isSuccessful() || responseBody == null) {
                        onFail();
                        return;
                    }

                    if (responseBody.getResponseCode() != BaseResponse.ResponseCode.OK) {
                        onFail();
                        return;
                    }


                    if (responseBody.transactions.size() > 0) {
                        cache.addAll(responseBody.transactions);

                        // TransactionHistoryResponse.TransactionHistoryEntry lastTransaction = cache.get(cache.size() - 1);
                        mAdapter = new historyAdapter(cache, getContext(), new historyAdapter.transactionOnclickLintener() {
                            @Override
                            public void onTransactionListener(int position, String Type, String Amount, String id,
                                                              String timeDate, Map<String, String> details, Boolean reverted,
                                                              String initiatorWallet, String DestinationWallet,
                                                              String AdditionalWallet, String fee, String balanceBefore,
                                                              String balanceAfter, Map<String, String> data) {
                                TransactionResult transactionResult = new TransactionResult(teasyMain, teasyMain);
                                String mAmount = "₦" + Utils.formatBalance(Long.valueOf(Amount));
                                String mBalanceBefore = "₦" + Utils.formatBalance(balanceBefore);
                                String mBalanceAfter = "₦" + Utils.formatBalance(balanceAfter);
                                String mFee = "₦" + Utils.formatBalance(fee);

                                switch (Type){


                                    case "Bank Transfer": {
                                        List<Bank> mBank = getBanks();
                                        historyBankTransferModel transferModel = new historyBankTransferModel();
                                        String Name = data.get(transferModel.getTargetAccountName());
                                        String code = data.get(transferModel.getDestinationCode());
                                        String bvn = data.get(transferModel.getBvn());
                                        String sourceName = data.get(transferModel.getSourceAccountName());
                                        transferModel.setBvn(bvn);
                                        transferModel.setDestinationCode(code);
                                        transferModel.setTargetAccountName(Name);
                                        transferModel.setSourceAccountName(sourceName);
                                        String temp = "";
                                        String AC = details.get("Additional Information");
                                        Log.e("Code", "onTransactionListener: " + code);
                                        for (int i = 0; i < mBank.size(); i++){
                                            if (mBank.get(i).getCode().equals(code)){

                                                temp = mBank.get(i).getDisplayName();
                                                break;
                                            }
                                        }
                                        transactionResult.successfulTransaction();
                                        transactionResult.getBankName().setText(temp);
                                        transactionResult.getName().setText(Name);
                                        transactionResult.getAccountNumber().setText(AC);
                                        transactionResult.getAmount().setText(mAmount);
                                        transactionResult.getTransactionCode().setText(id);
                                        transactionResult.getNarration().setText("");
                                        transactionResult.getDirection().setText("Debit");
                                        transactionResult.getDirection().setTextColor(Color.RED);
                                        failedMethod(reverted, transactionResult.getMessage(),
                                                transactionResult.getImage());
                                        transactionResult.getDirectionHeader().setVisibility(View.GONE);
                                        transactionResult.getDirection().setVisibility(View.GONE);
                                        transactionResult.getConfirmkPIN().show();

                                        break;
                                    } case "Cash Out":{
                                        splitString m = walletSplit(initiatorWallet);
                                        String initiatorName = m.name;
                                        String initiatorPhone = m.phoneNumber;
                                        m = walletSplit(DestinationWallet);
                                        String destinationName = m.name;
                                        String destinationPhone = m.phoneNumber;
                                        transactionResult.successfulTransaction();
                                        transactionResult.getBankName().setText("TeasyMobile");
                                        transactionResult.getName().setText(destinationName);
                                        transactionResult.getAccountNumber().setText(destinationPhone);
                                        transactionResult.getRecipianAc().setText("Recipient Wallet Number");
                                        transactionResult.getAmount().setText(mAmount);
                                        transactionResult.getTransactionCode().setText(id);
                                        transactionResult.getNarration().setVisibility(View.GONE);
                                        transactionResult.getNarationHeader().setVisibility(View.GONE);
                                        transactionResult.getDirectionHeader().setVisibility(View.GONE);
                                        transactionResult.getDirection().setVisibility(View.GONE);

                                        failedMethod(reverted, transactionResult.getMessage(),
                                                transactionResult.getImage());
                                        transactionResult.getConfirmkPIN().show();

                                        break;
                                    } default:{
                                        splitString m = walletSplit(initiatorWallet);
                                        String initiatorName = m.name;
                                        String initiatorPhone = m.phoneNumber;
                                        m = walletSplit(DestinationWallet);
                                        String destinationName = m.name;
                                        String destinationPhone = m.phoneNumber;
                                        transactionResult.successfulTransaction();

                                        transactionResult.getBankName().setText("TeasyMobile");
                                        transactionResult.getRescipiantName().setText("Type");

                                        transactionResult.getName().setText(destinationName);
                                        transactionResult.getAccountNumber().setVisibility(View.GONE);
                                        transactionResult.getRecipianAc().setVisibility(View.GONE);
                                        transactionResult.getAmount().setText(mAmount);
                                        transactionResult.getTransactionCode().setText(id);
                                        transactionResult.getNarration().setVisibility(View.GONE);
                                        transactionResult.getNarationHeader().setVisibility(View.GONE);
                                        transactionResult.getDirectionHeader().setVisibility(View.GONE);
                                        transactionResult.getDirection().setVisibility(View.GONE);
                                        if(DestinationWallet.equals("11120000001 - AEDC")){
                                            transactionResult.getName().setText("ADEC Purchase");
                                            String token = data.get("token");
                                            String meterNumber = AdditionalWallet;
                                            transactionResult.getAccountNumber().setVisibility(View.VISIBLE);
                                            transactionResult.getRecipianAc().setVisibility(View.VISIBLE);
                                            transactionResult.getAccountNumber().setText(meterNumber);
                                            transactionResult.getRecipianAc().setText("Meter Number");
                                            transactionResult.getDirectionHeader().setVisibility(View.VISIBLE);
                                            transactionResult.getDirectionHeader().setText("Token");
                                            transactionResult.getDirection().setVisibility(View.VISIBLE);
                                            transactionResult.getDirection().setText(token);


                                        }
                                        failedMethod(reverted, transactionResult.getMessage(),
                                                transactionResult.getImage());
                                        transactionResult.getConfirmkPIN().show();
                                    }

                                }


                            }
                        });

                        popRecycler.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);


                    }
                }

                @Override
                public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {

                    onFail();
                }

                private void onFail() {
                   progressBar.setVisibility(View.GONE);
                    Popups.showPopup(getActivity(), "Unable to load history. Please, try again later.", null);

                }
            };

       teasyMain.getAppMananagerAPI().transactionHistoryV2(
                session.walletId,
                session.pin,
                null,
                null,
                100
        ).enqueue(callback);


        }catch (Exception ignored){

        }
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);
    }

    private void Notification_Pop(MainActivity teasyMain){
        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.notification_pop, mGroup,false);

        ProgressBar progressBar = view.findViewById(R.id.progressBar2);
        RecyclerView popRecycler = view.findViewById(R.id.mrecycler);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        popRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

            Callback<GetNotificationsResponse> callback = new Callback<GetNotificationsResponse>() {
                @Override
                public void onResponse(Call<GetNotificationsResponse> call, Response<GetNotificationsResponse> response) {

                    if(!response.isSuccessful()) {
                        onFail();
                        return;
                    }
                    GetNotificationsResponse responseBody = response.body();
                    notificationCache.addAll(responseBody.getNotificationList());
                    NotificationAdapter myAdapter = new NotificationAdapter(notificationCache, getContext(), new NotificationAdapter.notificationOnClickLintener() {
                        @Override
                        public void onNotificationListener(String Date, long Amount, String Status, String Remark, String Type, int direction) {
                            String amount = "₦" + Utils.formatBalance(Amount);
                            TransactionResult transactionResult = new TransactionResult(teasyMain,teasyMain);
                            if(Status.equals("SUCCESS")){
                                switch (Type) {
                                    case "BANK_TRANSFER": {
                                        transactionResult.successfulTransaction();
                                        transactionResult.getBankName().setText(bankRemark(Remark)[1]);
                                        transactionResult.getAmount().setText(amount);
                                        transactionResult.getTransactionCode().setText(getDisplayName().toUpperCase());
                                        transactionResult.getTransactioncodeHeader().setText("Senders Name");
                                        transactionResult.getAccountNumber().setText(bankRemark(Remark)[2]);
                                        transactionResult.getName().setText(bankRemark(Remark)[0].toUpperCase());
                                        transactionResult.getNarationHeader().setText("Transaction Date And Time");
                                        transactionResult.getNarration().setText(Date);
                                        if(direction == 1){
                                            transactionResult.getDirection().setText("Credit");
                                            transactionResult.getDirection().setTextColor(Color.GREEN);
                                        }else{
                                            transactionResult.getDirection().setText("Debit");
                                            transactionResult.getDirection().setTextColor(Color.RED);
                                        }
                                        transactionResult.getConfirmkPIN().show();
                                        break;
                                    } case "PAY_BILL_AEDC":{
                                        transactionResult.successfulTransaction();
                                        transactionResult.getFinancialInstitution().setText("Token");
                                        transactionResult.getFinancialInstitution().setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                                        transactionResult.getBankName().setText(AEDCresponse(Remark)[0]);
                                        transactionResult.getBankName().setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                                        transactionResult.getBankName().setTypeface(null, Typeface.BOLD);
                                        transactionResult.getRescipiantName().setText("Units");
                                        transactionResult.getRescipiantName().setTextSize(TypedValue.COMPLEX_UNIT_SP,20);;
                                        transactionResult.getName().setText(AEDCresponse(Remark)[1]);
                                        transactionResult.getName().setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                                        transactionResult.getAmount().setText(Date);
                                        transactionResult.getAmountHeader().setText("Transaction Date and Time");
                                        transactionResult.getAccountNumber().setText(amount);
                                        transactionResult.getRecipianAc().setText("Amount");
                                        transactionResult.getTransactionCode().setText("AEDC Purchase");
                                        transactionResult.getTransactioncodeHeader().setText("Transaction Type");
                                        transactionResult.getNarration().setVisibility(View.GONE);
                                        transactionResult.getNarationHeader().setVisibility(View.GONE);
                                        transactionResult.getNarration().setTextColor(Color.RED);
                                        if(direction == 1){
                                            transactionResult.getDirection().setText("Credit");
                                            transactionResult.getDirection().setTextColor(Color.GREEN);
                                        }else{
                                            transactionResult.getDirection().setText("Debit");
                                            transactionResult.getDirection().setTextColor(Color.RED);
                                        }
                                        transactionResult.getConfirmkPIN().show();
                                        break;
                                    } case "WALLET_TRANSFER":{
                                        final String destNumber = walletToWallet(Remark).replaceAll("[^0-9]","");

                                        Dialog sendRequestDialog = Popups.showOperationInProcessDialog(sesTest,"Validating Receivers Number...");

                                        Callback<GetUserDataResponse> callback = new Callback<GetUserDataResponse>() {
                                            @Override
                                            public void onResponse(Call<GetUserDataResponse> call, Response<GetUserDataResponse> response) {
                                                sendRequestDialog.dismiss();
                                                if(!response.isSuccessful()) {
                                                    showFailPopup("Validation failed");
                                                    return;
                                                }

                                                GetUserDataResponse responseBody = response.body();
                                                switch (responseBody.getResponseCode()) {
                                                    case OK:
                                                        String accountType = responseBody.accountType;
                                                        String receiverName;

                                                        switch (accountType) {
                                                            case "AGENT":
                                                                receiverName = responseBody.organizationName;
                                                                break;
                                                            default:
                                                                receiverName = responseBody.firstName;

                                                                String middleName = responseBody.middleName;
                                                                if (!StringUtils.isBlank(middleName)) {
                                                                    receiverName += " " + middleName;
                                                                }

                                                                receiverName += " " + responseBody.lastName;

                                                                break;
                                                        }

                                                        transactionResult.successfulTransaction();
                                                        //transactionResult.getBankName().setText(walletToWallet(Remark));
                                                        transactionResult.getBankName().setText("TeasyPay");
                                                        transactionResult.getRescipiantName().setText("Source Wallet");
                                                        transactionResult.getRecipianAc().setText("Wallet Name\n" + receiverName);
                                                        transactionResult.getRecipianAc().setTypeface(null,Typeface.BOLD);
                                                        if(direction == 1){
                                                            transactionResult.getName().setText(walletToWallet(Remark));
                                                            transactionResult.getName().setTypeface(null,Typeface.BOLD);
                                                            transactionResult.getAccountNumber().setText("Destination Wallet Number\n" + session.walletId);
                                                            transactionResult.getAccountNumber().setTypeface(null,Typeface.BOLD);
                                                            transactionResult.getDirection().setText("Credit");
                                                            transactionResult.getDirection().setTextColor(Color.GREEN);
                                                        }else {
                                                            transactionResult.getName().setText(session.walletId);
                                                            transactionResult.getAccountNumber().setText("Destination Wallet Number\n" +walletToWallet(Remark));
                                                            transactionResult.getAccountNumber().setTypeface(null,Typeface.BOLD);
                                                            transactionResult.getDirection().setText("Debit");
                                                            transactionResult.getDirection().setTextColor(Color.RED);
                                                        }

                                                        transactionResult.getAmount().setText(amount);
                                                        transactionResult.getTransactionCode().setText(Date);
                                                        transactionResult.getTransactioncodeHeader().setText("Transaction Date and Time");
                                                        transactionResult.getNarationHeader().setVisibility(View.GONE);
                                                        transactionResult.getNarration().setVisibility(View.GONE);
                                                        transactionResult.getConfirmkPIN().show();
                                                        break;

                                                    case CUSTOMER_NOT_REGISTERED:

                                                        showFailPopup("Customer Not Registered");
                                                        break;

                                                    default:
                                                        showFailPopup(responseBody.message);
                                                        break;
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<GetUserDataResponse> call, Throwable t) {
                                                sendRequestDialog.dismiss();
                                                showFailPopup("Validation failed");
                                            }

                                            private void showFailPopup(String message) {
                                                PopupOperationFailed popup = new PopupOperationFailed(sesTest);
                                                if(!StringUtils.isBlank(message)) {
                                                    popup.setText(message);
                                                }
                                                popup.show();
                                            }
                                        };

                                        sesTest.getAppMananagerAPI().getUserDataRequest(destNumber).enqueue(callback);

                                        break;
                                    } case "AIRTIME_PURCHASE":{
                                        transactionResult.successfulTransaction();
                                        transactionResult.getTransactioncodeHeader().setVisibility(View.GONE);
                                        transactionResult.getTransactionCode().setVisibility(View.GONE);
                                        transactionResult.getBankName().setText("TeasyMobile");
                                        transactionResult.getName().setText("Airtime/Data Purchase");
                                        transactionResult.getRescipiantName().setText("Transact Type");
                                        transactionResult.getAmount().setText(amount);
                                        transactionResult.getNarationHeader().setText("Transaction Date and Time");
                                        transactionResult.getRecipianAc().setVisibility(View.GONE);
                                        transactionResult.getAccountNumber().setVisibility(View.GONE);
                                        transactionResult.getNarration().setText(Date);
                                        if(direction == 1){
                                            transactionResult.getDirection().setText("Credit");
                                            transactionResult.getDirection().setTextColor(Color.GREEN);
                                        }else{
                                            transactionResult.getDirection().setText("Debit");
                                            transactionResult.getDirection().setTextColor(Color.RED);
                                        }
                                        transactionResult.getConfirmkPIN().show();
                                    }
                                }
                            }else{
                                transactionResult.failedTransaction("Transaction Failed");
                            }

                        }
                    });
                    popRecycler.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<GetNotificationsResponse> call, Throwable t) {

                    onFail();
                }

                private void onFail() {
                }
            };

            teasyMain.getAppMananagerAPI().getNotificationsRequest(
                    session.walletId,
                    session.pin,
                    session.getAccountType().name(),
                    session.getAgentShortCode()
            ).enqueue(callback);

        popupLayout.show(PopupLayout.POSITION_TOP);
        popupLayout.setUseRadius(true);


    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private void Profile_pop(){
        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.profile_, mGroup,false);
        CustomTextView userType = view.findViewById(R.id.mUser_Type);
        CustomTextView userName = view.findViewById(R.id.User_Name);
        CustomTextView shortCode = view.findViewById(R.id.customTextView2);
        CustomTextView AccountID = view.findViewById(R.id.Account_ID);
        com.SmartTech.teasyNew.view.menu.MenuItem transactionHistory = view.findViewById(R.id.T_history);
        com.SmartTech.teasyNew.view.menu.MenuItem changePin = view.findViewById(R.id.Change_pin);
        CustomTextView logout = view.findViewById(R.id.logout);
        com.SmartTech.teasyNew.view.menu.MenuItem support = view.findViewById(R.id.support);
        com.SmartTech.teasyNew.view.menu.MenuItem register = view.findViewById(R.id.Register);

        CustomTextView faq = view.findViewById(R.id.FAQ);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        com.SmartTech.teasyNew.view.menu.MenuItem mfundWallet = view.findViewById(R.id.Fund_wallet);
        if(session.getAccountType() != Session.AccountType.AGENT){
            register.setVisibility(View.GONE);
        }

        if(new database(sesTest).getNotificationToggleStatus() == 3 ){
            new database(sesTest).insertToggle(2,0);
        }

        transactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Notification_Pop(sesTest);
                popupLayout.dismiss();

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityRegistration.class);
                intent.putExtra("Session", session);
                intent.putExtra("PreviousActivity", MainActivity.class);
                intent.putExtra("NextActivity", MainActivity.class);
                startActivity(intent);
                sesTest.finish();
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FAQi faQi = new FAQi(sesTest);
                faQi.FAQ(getContext());
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Support support1 = new Support(getContext());
                support1.showSupport();
                popupLayout.dismiss();
            }
        });
        mfundWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fundWallet fundW = new fundWallet(getContext(),Gravity.TOP,session,sesTest);

                fundW.Fwallet();
                popupLayout.dismiss();
            }
        });
        userName.setText(getDisplayName().toUpperCase());
        AccountID.setText(session.walletId);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                session.getAgentShortCode();

                try {


                    if(session.getAccountType() == Session.AccountType.AGENT) {
                        userType.setText("User Type: Agent");
                        String shortcode = "Agent Code: " + session.getAgentShortCode();
                        shortCode.setText(shortcode);

                    }
                    else {
                        userType.setText("User Type: Customer");
                        shortCode.setVisibility(View.GONE);
                    }
                } catch (NumberFormatException ex) {
                    userType.setText("Not Available");

                }
            }
        };

        handler.post(runnable);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
                ExitApp exitApp = new ExitApp(sesTest);
                exitApp.Exit("Do You Want To Logout ?");
                exitApp.getYes().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ActivityLogin.class);
                        ArrayList<ContactModel> mContacts = sesTest.getSession().getContacts();
                        intent.putExtra("Smart_Contacts", mContacts);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                exitApp.getNo().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exitApp.getPinDialog().dismiss();
                    }
                });
                exitApp.getPinDialog().show();

            }
        });
        changePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePIN();
                popupLayout.dismiss();
            }
        });

        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_LEFT);
    }

    private void changePIN(){

        Dialog pinDialog = new Dialog(sesTest);
        pinDialog.setContentView(R.layout.change_pin_pop);
        Window window = pinDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        pinDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText editTextPin = pinDialog.findViewById(R.id.editTextNumberPassword1);
        EditText editTextNewPin = pinDialog.findViewById(R.id.editTextNumberPassword2);
        EditText editTextConfirmPin = pinDialog.findViewById(R.id.editTextNumberPassword3);
        CustomTextView button = pinDialog.findViewById(R.id.btn_proceed);
        ImageButton closeBtn = pinDialog.findViewById(R.id.imageButton6);



        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinDialog.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedClicked(editTextPin, editTextNewPin, editTextConfirmPin);
                pinDialog.dismiss();
            }
        });
        pinDialog.show();



    }

    private void proceedClicked(EditText PIN, EditText newPIN, EditText confirmPIN) {
        String pin = PIN.getText().toString().trim();
        String newPin = newPIN.getText().toString().trim();
        String confirmPin = confirmPIN.getText().toString().trim();

        if(StringUtils.isBlank(pin)) {
            Popups.showPopup(getActivity(), "Please enter your PIN", null);
            return;
        }

        if(StringUtils.isBlank(newPin)) {
            Popups.showPopup(getActivity(), "Please enter your new PIN", null);
            return;
        }

        if(StringUtils.isBlank(confirmPin)) {
            Popups.showPopup(getActivity(), "Please, confirm your new PIN", null);
            return;
        }

        if(!confirmPin.equals(newPin)){

            Popups.showPopup(getActivity(), "Please, confirm your new PIN", null);
            return;
        }
        String regex = "^[0-9]{4}$";
        if(!pin.matches(regex) || !newPin.matches(regex) || !confirmPin.matches(regex)) {
            Popups.showPopup(getActivity(), "PIN must be a 4-digit string", null);
            return;
        }

        ChangePinRunnable runnable = new ChangePinRunnable(session.walletId, pin, newPin);
        runnable.setTargetObject(getActivity());
        runnable.run();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.NisCard:{
                MakePaymentInplementation makePaymentInplementation =
                        new MakePaymentInplementation(sesTest, true);
               PopupLayout popupLayout =  makePaymentInplementation.applicationID(mGroup, getContext());
                makePaymentInplementation.getApplicationID().setHint("Enter NIS Application ID");
               popupLayout.setUseRadius(true);
               popupLayout.show(PopupLayout.POSITION_BOTTOM);
                break;
            } case R.id.GotvCard:{
                tvLoad(6);
                break;
            } case R.id.AedcCard:{
                pay_bill_TV bill_tv = new pay_bill_TV(getContext(),
                        "Enter Meter Number",
                        sesTest,
                        session,
                        0,
                        "ELECTRICITY",
                        true
                );

                bill_tv.AEDCPayment().show();
                break;
            } case R.id.DstvCard:{
                tvLoad(0);
                break;
            } case R.id.send:{
                if(session.getAccountType() == CUSTOMER){
                    Fragment fragment = new BankTransfer();
                    BankTransferModel transferModel = new BankTransferModel();
                    Bundle bundle = new Bundle();
                    transferModel.setAccountType("MAIN");
                    bundle.putSerializable("key", transferModel);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView,
                                    fragment).addToBackStack("bankTransfer").commit();
                }else {
                    BankTransferOption();
                }

                break;
            } case R.id.FrscCard:{
                MakePaymentInplementation makePaymentInplementation =
                        new MakePaymentInplementation(sesTest, false);
                PopupLayout popupLayout =  makePaymentInplementation.applicationID(mGroup, getContext());
                makePaymentInplementation.getApplicationID().setHint("Enter FRSC Application ID");
                popupLayout.setUseRadius(true);
                popupLayout.show(PopupLayout.POSITION_BOTTOM);
                break;

            } case R.id.recieve: {
               ptest();
               break;
            } case R.id.transactions:{
                if(cache.size() >0) {
                    cache.clear();
                    mAdapter.notifyDataSetChanged();
                }
                transaction(sesTest);
            }
        }
    }

    private class ChangePinRunnable extends MakeTransactionRunnable2<BaseResponse> {

        private String walletId, pin, newPin;

        private ChangePinRunnable(String walletId, String pin, String newPin) {
            this.walletId = walletId;
            this.pin = pin;
            this.newPin = newPin;
        }

        @Override
        protected Call<BaseResponse> getRequest() {
            return activity.getAppMananagerAPI()
                    .pinChangeRequest(
                        walletId,
                        pin,
                        newPin
                    );
        }

        @Override
        public SerializableRunnable transactionRetryClicked() {
            return null;
        }

        @Override
        protected void onErrorCode(Response<BaseResponse> response, Intent intent, TransactionResultData transactionResultData) {
            BaseResponse responseBody = response.body();
            switch (responseBody.getResponseCode()) {
                case INCORRECT_CREDENTIALS:
                    Popups.showPopup(activity, "Incorrect PIN", null);
                    return;

                default:
                    Popups.showPopup(activity, "Unsuccessful", null);
                    return;
            }
        }

        @Override
        protected void onFail(Intent intent, TransactionResultData transactionResultData) {
            Popups.showPopup(activity, "Unsuccessful", null);
        }

        @Override
        protected void onSuccess(Response<BaseResponse> response, Intent intent, TransactionResultData transactionResultData) {
            activity.getSession().pin = newPin;
            String encryptedString = Encrytion.encrypt(newPin);
            new database(getContext()).insertPin(1,encryptedString);
            Dialog dialog = Popups.showPopup(activity, "PIN successfully changed", null);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                    Intent intent = new Intent(sesTest, ActivityLogin.class);
                    startActivity(intent);
                    sesTest.finish();

                }
            });
        }
    }


    private String [] bankRemark(String Remark){
         String[] mArray = new String[3];
         int position1, position2, position3;
         position1 = Remark.indexOf(":");
         position2 = Remark.indexOf(":",position1+1);
         position3 = Remark.lastIndexOf(":");
         mArray[0] = Remark.substring(position1+1, position2-("Financial Institution".length()+1));
         mArray[1] = Remark.substring(position2+1, position3-("Account".length()+1));
         mArray[2] = Remark.substring(position3+1);

        return mArray;
    }

    private String [] AEDCresponse(String Remark){
        String[] mArray = new String[2];
        //char[] m2 = Remark.toCharArray();
        int position1 = 0, position2 =0;
        position1 = Remark.indexOf(":");
        position2 = Remark.lastIndexOf(":");
        mArray[0] = Remark.substring(position1+1, position2-("Units".length()+1));
        mArray[0] = formatAEDC(mArray[0].replaceAll("\\s+",""));
        mArray[1] = Remark.substring(position2+1);


        return mArray;
    }

    private String  walletToWallet(String Remark){
          return Remark.substring(Remark.indexOf(":")+1);
    }



    private void tvLoad(int position){
        pay_bill_TV bill_tv = new pay_bill_TV(getContext(),
                "Smart Card Number",
                sesTest,
                session,
                position,
                "CABLE_TV",
                false
        );
        bill_tv.billPayment().show();
    }




    private String formatAEDC(String fomat){
        String Temp=fomat;
        if(!(fomat==null)&&!(fomat.equals(""))){

            for(int i=4; i<fomat.length(); i +=4 ){
                if(i==4){
                    Temp = new StringBuilder(Temp).insert(i, " ").toString();
                }
                if(i>4){
                    Temp = new StringBuilder(Temp).insert(++i, " ").toString();
                }
            }
        }

        return Temp;
    }



    private  void  ptest(){
        //1. intializ where popfrom

        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.recieve_money_option, mGroup,false);
        //View view = View.inflate(getContext(),R.layout.confirm_layout,null);
        LinearLayout Bank_MMO = view.findViewById(R.id.bank);
        LinearLayout Teasy = view.findViewById(R.id.teasy);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);

        Bank_MMO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
                accountdetails();
            }
        });

        Teasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.dismiss();
                QRCODE();
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
    private void accountdetails(){
        //1. intializ where popfrom

        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.account_details, mGroup,false);
        //View view = View.inflate(getContext(),R.layout.confirm_layout,null);
        TextView AccountName = view.findViewById(R.id.textView243);
        TextView AccountNumber = view.findViewById(R.id.textView241);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        String name;

        if(session.getAccountType() == Session.AccountType.AGENT) {
            name = session.getAgentName();
        }else{
            name  = session.getCustomerFirstName() + " " + session.getCustomerLastName();
        }
        AccountName.setText(name.toUpperCase());
        String acNumber = session.walletId.substring(1);
        AccountNumber.setText(acNumber);
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);
    }


    private void QRCODE(){

        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.qrcode_interface, mGroup,false);
        //View view = View.inflate(getContext(),R.layout.confirm_layout,null);
        ImageView grView = view.findViewById(R.id.imageView41);
        TextView wallet = view.findViewById(R.id.textView247);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        QRModel model = new QRModel();
        String name;
        wallet.setText("Wallet Number: " +session.walletId);
        if(session.getAccountType() == Session.AccountType.AGENT) {
            name = session.getAgentName();
        }else{
            name  = session.getCustomerFirstName() + " " + session.getCustomerLastName();
        }
        model.setName(name);
        model.setWalletNumber(session.walletId);
        Gson gson = new Gson();
        String qrString = gson.toJson(model);
        try {
            GenerateQRCode qRcode = new GenerateQRCode();
            Bitmap qrCode = qRcode.generateQRCode(qrString);
            // display the QR code on an ImageView
            grView.setImageBitmap(qrCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        popupLayout.setUseRadius(true);
        popupLayout.show(PopupLayout.POSITION_BOTTOM);
    }


    private List<Bank> getBanks(){
        List<Bank> mBank = Bank.byType(Bank.Type.BANK);
        mBank.addAll(Bank.byType(Bank.Type.MMO));
        return mBank;
    }

    private splitString walletSplit(String wallet){

        splitString m = new splitString();
        for(int i = 0; i < wallet.length(); i++){
            if (wallet.toCharArray()[i] == '-'){
                String tempV =  wallet.substring(0, i-1);
                m.phoneNumber = tempV.trim();
                tempV = wallet.substring(i+1);
                m.name = tempV.trim();
            }
        }

        return m;
    }
    private void failedMethod(boolean isFailed, TextView msg, ImageView imageView){
        if (isFailed){
            Glide.with(sesTest)
                    .load(R.drawable.icon_transaction_fail)
                    .into(imageView);
            msg.setText("Transaction Failed");
            msg.setTextColor(Color.RED);
        }else{
            Glide.with(sesTest)
                    .load(R.drawable.btn_login_active)
                    .into(imageView);
            msg.setText("Transaction Successful");
            msg.setTextColor(Color.GREEN);
        }
    }
}

class splitString{
    String phoneNumber;
    String name;
}
