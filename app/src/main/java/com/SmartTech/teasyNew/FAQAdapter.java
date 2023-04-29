package com.SmartTech.teasyNew;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetInterswitchPaymentItemsResponse;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {

    private Context mContext;
    private List<String> questions;
    private FAQAdapter.dataOnclickListener mdataOnclickListener;


    public FAQAdapter(List<String> questions,
                      FAQAdapter.dataOnclickListener mdataOnclickListener,
                      Context mContext) {

        this.mContext = mContext;
        this.questions = questions;
        this.mdataOnclickListener = mdataOnclickListener;

    }

    @NonNull
    @Override
    public FAQAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.faq_items, viewGroup,false);
        return new FAQAdapter.ViewHolder(view,mdataOnclickListener);
    }

    @Override
    public void onBindViewHolder(FAQAdapter.ViewHolder viewHolder, int i) {

        viewHolder.questions1.setText(questions.get(i).toUpperCase());
    }

    @Override
    public int getItemCount() {
        return  questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FAQAdapter.dataOnclickListener dataOnclickListener;
        TextView questions1;
        public ViewHolder(@NonNull View itemView, FAQAdapter.dataOnclickListener dataOnclickListener) {
            super(itemView);
            questions1 = itemView.findViewById(R.id.textView124);
            this.dataOnclickListener = dataOnclickListener;
            questions1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataOnclickListener.onDATAlister(getBindingAdapterPosition());
                }
            });
        }
    }

    public interface dataOnclickListener{
        void onDATAlister(int position);
    }



}
