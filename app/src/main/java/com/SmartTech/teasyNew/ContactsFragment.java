package com.SmartTech.teasyNew;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.codingending.popuplayout.PopupLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View parentView;
    private contactAdapter adapter;
    private MainActivity activity;
    private Session session;
    private ViewGroup mGroup;
    private PhoneNumberAdapter phoneNumberAdapter;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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
        parentView = inflater.inflate(R.layout.fragment_contacts, container, false);
        activity = (MainActivity) getActivity();
        session = activity.getSession();
        mGroup = container;
        RecyclerView contactsRecyclerView = parentView.findViewById(R.id.contactsRecyclerView);
        SearchView searchContacts = parentView.findViewById(R.id.ContactSearchView);
        ImageView back = parentView.findViewById(R.id.imageView36);
        adapter = new contactAdapter(session.getContacts(), activity, new contactAdapter.contactOnClickListener() {
            @Override
            public void onContactListener(int position, String id) {
                getPhoneNumbers(id);
            }
        });
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        contactsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        searchContacts.setIconifiedByDefault(false);
        searchContacts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String mS= query.toLowerCase();
                adapter.filterList(mS);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String mS= newText.toLowerCase();
                adapter.filterList(mS);
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ManageCard();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
            }
        });
        return parentView;
    }
    /*

       */

    private void getPhoneNumbers(String id) {
        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.select_beneficiary, mGroup,false);
        RecyclerView recyclerView = view.findViewById(R.id.savrRecycler);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        ArrayList<String> phone = new ArrayList<>();
        Uri URI_PHONE = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String SELECTION_PHONE = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String[] SELECTION_ARRAY_PHONE = new String[]{id};
        ContentResolver cr = activity.getContentResolver();
        Cursor currPhone = cr.query(URI_PHONE, null, SELECTION_PHONE, SELECTION_ARRAY_PHONE, null);
        int indexPhoneNo = currPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        //int indexPhoneType = currPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);

        if (currPhone.getCount() > 0) {
            currPhone.moveToFirst();
            String lastNumber = "";
            do{
                if(!lastNumber.equals(currPhone.getString(indexPhoneNo).replaceAll("\\s", ""))){
                    phone.add(currPhone.getString(indexPhoneNo).replaceAll("\\s", ""));

                }
                lastNumber = currPhone.getString(indexPhoneNo).replaceAll("\\s", "");

            }while(currPhone.moveToNext());
            currPhone.close();

            phoneNumberAdapter = new PhoneNumberAdapter(phone, activity, new PhoneNumberAdapter.contactOnClickListener() {
                @Override
                public void onContactListener(int position, String PhoneNumber) {
                    String tempNumber = manageContacts(PhoneNumber);
                    if(tempNumber.equals("")){
                        return;
                    }else {
                        Fragment fragment = new ManageCard();
                        Bundle bundle = new Bundle();
                        bundle.putString("number", tempNumber);
                        fragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerView,
                                        fragment).commit();
                    }

                    popupLayout.dismiss();
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(activity));




            recyclerView.setAdapter(phoneNumberAdapter);
            phoneNumberAdapter.notifyDataSetChanged();

            popupLayout.show(PopupLayout.POSITION_BOTTOM);
        }
    }

    private String manageContacts(String PhoneNumber){
        String Phone1 = PhoneNumber.replaceAll("\\s+","");
        Phone1 = Phone1.replaceAll("\\*","");
        Phone1 = Phone1.replaceAll("#","");
        String PhonNumber;
        if(Phone1.length() >= 11){
            if(Phone1.length()>11){
                PhonNumber = "0"+Phone1.substring(4);
            }else{
                PhonNumber = Phone1;
            }
        }else{
            Message.message(getContext(), "Invalid Phone Number Selected");
            PhonNumber = "";
        }

        return PhonNumber;
    }
}