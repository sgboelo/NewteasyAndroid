package com.SmartTech.teasyNew;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder> {

    private ArrayList<String> mdata;
    private Context mContext;
    PhoneNumberAdapter.contactOnClickListener McontactOnClickListener;


    public PhoneNumberAdapter(ArrayList<String> mdata, Context mContext,
                              PhoneNumberAdapter.contactOnClickListener McontactOnClickListener) {
        this.mdata = mdata;
        this.mContext = mContext;
        this.McontactOnClickListener = McontactOnClickListener;


    }

    @NonNull
    @Override
    public PhoneNumberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.save_benficiary_items, viewGroup,false);
        return new PhoneNumberAdapter.ViewHolder(view,McontactOnClickListener);
    }

    @Override
    public void onBindViewHolder(PhoneNumberAdapter.ViewHolder viewHolder, int i) {
        viewHolder.Name.setVisibility(View.GONE);
        viewHolder.BankName.setText(mdata.get(i));
        String Number = String.valueOf(i + 1);
        viewHolder.Number.setText(Number);
    }
    @Override
    public int getItemCount() {
        return  mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PhoneNumberAdapter.contactOnClickListener contactOnClickListener;
        TextView BankName, Name;
        LinearLayout mCard;
        TextView Number;
        public ViewHolder(@NonNull View itemView, PhoneNumberAdapter.contactOnClickListener contactOnClickListener) {
            super(itemView);
            BankName = itemView.findViewById(R.id.Bank);
            Name = itemView.findViewById(R.id.Name);
            mCard = itemView.findViewById(R.id.card);
            Number = itemView.findViewById(R.id.textView259);
            this.contactOnClickListener = contactOnClickListener;
            BankName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contactOnClickListener.onContactListener(getBindingAdapterPosition(),mdata.get(getBindingAdapterPosition()));
                }
            });
        }
    }

    public interface contactOnClickListener{
        void onContactListener(int position, String phoneNumber);
    }


}
