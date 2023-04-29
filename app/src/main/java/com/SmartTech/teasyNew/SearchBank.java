package com.SmartTech.teasyNew;

import android.widget.Filter;

import com.SmartTech.teasyNew.model.Bank;

import java.util.ArrayList;

public class SearchBank extends Filter {
    private ArrayList<Bank> bankArrayList;
    private ArrayList<Bank> filteredBankLIstList;
    BankAdapter adapter;

    public SearchBank(ArrayList<Bank> contactList, BankAdapter adapter) {
        this.bankArrayList = contactList;
        filteredBankLIstList = new ArrayList<>();
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        filteredBankLIstList.clear();
        final FilterResults results = new FilterResults();

        //here you need to add proper items do filteredContactList
        for (final Bank item : bankArrayList) {
            if (item.getDisplayName().toLowerCase().trim().contains(charSequence)) {
                filteredBankLIstList.add(item);
            }
        }

        results.values = filteredBankLIstList;
        results.count = filteredBankLIstList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapter.setList(filteredBankLIstList);
        adapter.notifyDataSetChanged();
    }
}
