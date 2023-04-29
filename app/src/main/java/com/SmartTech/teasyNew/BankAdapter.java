package com.SmartTech.teasyNew;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.model.Bank;

import java.util.ArrayList;
import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {

    private Context mContext;
    private List<Bank> Banks;
    private BankAdapter.OnBanks BanksOnClickListener;

    SearchBank filter;
    List<Bank> mContactFilter;
    public BankAdapter(List<Bank> Banks,
                       BankAdapter.OnBanks BanksOnClickListener,
                       Context mContext) {

        this.mContext = mContext;
        this.Banks = Banks;
        this.mContactFilter = Banks;
        this.BanksOnClickListener = BanksOnClickListener;
        ArrayList<Bank> m = new ArrayList<>();
        m.addAll(Banks);
        filter = new SearchBank(m,this);

    }

    // set adapter filtered list
    public void setList(ArrayList<Bank> list) {
        this.mContactFilter = list;
    }

    //call when you want to filter
    public void filterList(String text) {
        filter.filter(text);
    }
    @NonNull
    @Override
    public BankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.bank_items, viewGroup,false);
        return new BankAdapter.ViewHolder(view,BanksOnClickListener);
    }

    @Override
    public void onBindViewHolder(BankAdapter.ViewHolder viewHolder, int i) {

        if (!mContactFilter.get(i).getCode().equals("100010")) {
            viewHolder.BankItems.setText(mContactFilter.get(i).getDisplayName().toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        return  mContactFilter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        BankAdapter.OnBanks BanksOnclickListener;
        TextView BankItems;
        View view;
        LinearLayout  layout;
        public ViewHolder(@NonNull View itemView, BankAdapter.OnBanks BanksOnclickListener) {
            super(itemView);
            BankItems = itemView.findViewById(R.id.textView192);
            view = itemView.findViewById(R.id.view3);
            layout = itemView.findViewById(R.id.bankClick);

            this.BanksOnclickListener = BanksOnclickListener;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BanksOnclickListener.BanksOnClickListener(getBindingAdapterPosition(),
                            mContactFilter.get(getBindingAdapterPosition()).getCode(),
                            mContactFilter.get(getBindingAdapterPosition()).getDisplayName());
                }
            });
        }
    }

    public interface OnBanks{
        void BanksOnClickListener(int position, String BankCode, String BankName);
    }



}
