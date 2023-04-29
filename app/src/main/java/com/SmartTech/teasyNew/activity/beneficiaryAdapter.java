package com.SmartTech.teasyNew.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.model.Bank;
import com.SmartTech.teasyNew.model.SavedBeneficiary;

import java.util.List;

public class beneficiaryAdapter extends RecyclerView.Adapter<beneficiaryAdapter.ViewHolder> {

    private List<SavedBeneficiary> mdata;
    private Context mContext;
    private beneficiaryAdapter.beneficiaryOnClickListener mbeneficiaryOnClickListener;
    private List<Bank> banks;

    public beneficiaryAdapter(List<SavedBeneficiary>  mdata, Context mContext,
                              beneficiaryAdapter.beneficiaryOnClickListener mbeneficiaryOnClickListener) {
        this.mdata = mdata;
        this.mContext = mContext;
        this.mbeneficiaryOnClickListener = mbeneficiaryOnClickListener;
        this.banks = getBanks();

    }

    @NonNull
    @Override
    public beneficiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.beneficiaryitems, viewGroup,false);
        return new beneficiaryAdapter.ViewHolder(view, mbeneficiaryOnClickListener);
    }

    @Override
    public void onBindViewHolder(beneficiaryAdapter.ViewHolder viewHolder, int i) {

        viewHolder.BankName.setText(getBankName(mdata.get(i).bankID));
        viewHolder.Name.setText(mdata.get(i).beneficiaryName);
        viewHolder.accountNumber.setText(mdata.get(i).bankAccountId);

    }

    @Override
    public int getItemCount() {
        return  mdata.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        beneficiaryAdapter.beneficiaryOnClickListener beneficiaryOnClickListener;
        TextView BankName, Name, accountNumber;
        LinearLayout cardView;
        public ViewHolder(@NonNull View itemView, beneficiaryAdapter.
                beneficiaryOnClickListener beneficiaryOnClickListener) {
            super(itemView);
            BankName = itemView.findViewById(R.id.textView205);
            Name = itemView.findViewById(R.id.textView203);
            accountNumber = itemView.findViewById(R.id.textView204);
            cardView = itemView.findViewById(R.id.Items);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    beneficiaryOnClickListener.onTransactionListener(getBindingAdapterPosition(),
                            mdata.get(getBindingAdapterPosition()).bankID,
                            mdata.get(getBindingAdapterPosition()).beneficiaryName,
                            mdata.get(getBindingAdapterPosition()).bankAccountId,
                            mdata.get(getBindingAdapterPosition()).walletID,
                            getBankName(mdata.get(getBindingAdapterPosition()).bankID));

                }
            });


        }



    }



    public interface beneficiaryOnClickListener{
        void onTransactionListener(int position, String BankID , String BenName, String AcountID,
                                   String WalletID, String bankName);
    }

    private List<Bank> getBanks(){
        List<Bank> mBank = Bank.byType(Bank.Type.BANK);
        mBank.addAll(Bank.byType(Bank.Type.MMO));
        return mBank;
    }

    private String getBankName(String ID){
        String bankName = "";
        for(int i=0; i < banks.size(); i++){
            if(banks.get(i).getCode().equals(ID)){
                bankName = banks.get(i).getDisplayName();
                break;
            }
        }
        return bankName;
    }

}
