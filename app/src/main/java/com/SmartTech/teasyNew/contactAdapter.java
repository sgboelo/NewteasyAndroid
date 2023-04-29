package com.SmartTech.teasyNew;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class contactAdapter extends RecyclerView.Adapter<contactAdapter.ViewHolder> {

    private ArrayList<ContactModel> mdata;
    private Context mContext;
    contactAdapter.contactOnClickListener McontactOnClickListener;
    SearcClass filter;
    List<ContactModel> mContactFilter;


    public contactAdapter(ArrayList<ContactModel> mdata, Context mContext, contactAdapter.contactOnClickListener McontactOnClickListener) {
        this.mdata = mdata;
        this.mContext = mContext;
        this.mContactFilter = mdata;
        this.McontactOnClickListener = McontactOnClickListener;
        filter = new SearcClass(mdata,this);


    }

    @NonNull
    @Override
    public contactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.save_benficiary_items, viewGroup,false);
        return new contactAdapter.ViewHolder(view,McontactOnClickListener);
    }

    @Override
    public void onBindViewHolder(contactAdapter.ViewHolder viewHolder, int i) {

        viewHolder.Name.setVisibility(View.GONE);
        viewHolder.BankName.setText(mContactFilter.get(i).getDisplayName());
        String l = "";
        if(mContactFilter.get(i).getDisplayName() != null) {
            l = mContactFilter.get(i).getDisplayName();
            l = l.substring(0, 1);
        }
        viewHolder.letter.setText(l);

    }

    @Override
    public int getItemCount() {
        return  mContactFilter.size();
    }

    // set adapter filtered list
    public void setList(ArrayList<ContactModel> list) {
        this.mContactFilter = list;
    }

    //call when you want to filter
    public void filterList(String text) {
        filter.filter(text);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        contactAdapter.contactOnClickListener contactOnClickListener;
        TextView BankName, Name, letter;
        LinearLayout mCard;
        public ViewHolder(@NonNull View itemView, contactAdapter.contactOnClickListener contactOnClickListener) {
            super(itemView);
            BankName = itemView.findViewById(R.id.Bank);
            Name = itemView.findViewById(R.id.Name);
            mCard = itemView.findViewById(R.id.card);
            letter = itemView.findViewById(R.id.textView259);
            this.contactOnClickListener = contactOnClickListener;
            mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    contactOnClickListener.onContactListener(getBindingAdapterPosition(),
                            mContactFilter.get(getBindingAdapterPosition()).getContractID());

                }
            });
        }
    }

    public interface contactOnClickListener{
        void onContactListener(int position, String id);
    }


}
