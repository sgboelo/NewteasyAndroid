package com.SmartTech.teasyNew;

import android.widget.Filter;

import com.SmartTech.teasyNew.model.ContactModel;

import java.util.ArrayList;

public class SearcClass extends Filter {
    private ArrayList<ContactModel> contactList;
    private ArrayList<ContactModel> filteredContactList;
    contactAdapter ContactAdapter;

    public SearcClass(ArrayList<ContactModel> contactList, contactAdapter ContactAdapter) {
        this.contactList = contactList;
        filteredContactList = new ArrayList<>();
        this.ContactAdapter = ContactAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        filteredContactList.clear();
        final FilterResults results = new FilterResults();

        //here you need to add proper items do filteredContactList
        for (final ContactModel item : contactList) {
            if (item.getDisplayName().toLowerCase().trim().contains(charSequence)) {
                filteredContactList.add(item);
            }
        }

        results.values = filteredContactList;
        results.count = filteredContactList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        ContactAdapter.setList(filteredContactList);
        ContactAdapter.notifyDataSetChanged();
    }
}
