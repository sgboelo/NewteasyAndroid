package com.SmartTech.teasyNew;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.SmartTech.teasyNew.activity.ActivityLogin;
import com.SmartTech.teasyNew.model.ContactModel;

import java.util.ArrayList;

public class LoadContacts {
    private ActivityLogin activity;
    public LoadContacts(ActivityLogin activity){
        this.activity = activity;
    }

    @SuppressLint("Range")
    public ArrayList<ContactModel> getAllContacts() {
        ArrayList<ContactModel> contactList = new ArrayList<>();
        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                ContactModel contact = new ContactModel();
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                contact.setDisplayName(name);
                contact.setContractID(id);
                contactList.add(contact);
            }
        }
        if (cur != null) {
            cur.close();
        }
        return contactList;
    }
}
