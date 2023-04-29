package com.SmartTech.teasyNew;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.activity.beneficiaryAdapter;
import com.SmartTech.teasyNew.model.Bank;
import com.SmartTech.teasyNew.model.SavedBeneficiary;

import java.util.List;

public class LoadBen {
    private Activity activity;
    private int gravity;
    private beneficiaryAdapter adapter;
    private LoadBenOnclick loadBenOnclick;
    private List<Bank> bankList;
    public LoadBen(Activity activity, int gravity, LoadBenOnclick loadBenOnclick) {
        this.loadBenOnclick = loadBenOnclick;
        this.activity = activity;
        this.gravity = gravity;
    }

    public void selectBen(List<SavedBeneficiary> savedBeneficiaryList){

        Dialog quickDialog = new Dialog(activity);
        quickDialog.setContentView(R.layout.select_beneficiary);
        Window window = quickDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        adapter = new beneficiaryAdapter(savedBeneficiaryList, activity, new beneficiaryAdapter.beneficiaryOnClickListener() {
            @Override
            public void onTransactionListener(int position, String BankID, String BenName, String AcountID, String WalletID, String BankName) {

                loadBenOnclick.LoadBenOnclickListener(position,BankID,BenName, AcountID, WalletID, getBankPlus(BankID));
                quickDialog.dismiss();


            }
        });
        RecyclerView recyclerView = quickDialog.findViewById(R.id.savrRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        quickDialog.show();



    }

    public interface LoadBenOnclick{
        void LoadBenOnclickListener(int position, String BankID , String BenName, String AcountID,
                                   String WalletID, String Bank);
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
