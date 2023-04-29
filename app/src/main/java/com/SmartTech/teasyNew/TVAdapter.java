package com.SmartTech.teasyNew;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetInterswitchPaymentItemsResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.PADataValidationResponse;

import java.util.List;

public class TVAdapter extends RecyclerView.Adapter<TVAdapter.ViewHolder> {

    private Context mContext;
    private List<GetInterswitchPaymentItemsResponse.InterswitchPaymentItem> products;
    private TVAdapter.dataOnclickListener mdataOnclickListener;

    public TVAdapter(List<GetInterswitchPaymentItemsResponse.InterswitchPaymentItem> products,
                     TVAdapter.dataOnclickListener mdataOnclickListener,
                     Context mContext) {

        this.mContext = mContext;
        this.products = products;
        this.mdataOnclickListener = mdataOnclickListener;

    }

    @NonNull
    @Override
    public TVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.product_items, viewGroup,false);
        return new TVAdapter.ViewHolder(view,mdataOnclickListener);
    }

    @Override
    public void onBindViewHolder(TVAdapter.ViewHolder viewHolder, int i) {
        viewHolder.product.setText(products.get(i).paymentitemname);



    }

    @Override
    public int getItemCount() {
        return  products.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TVAdapter.dataOnclickListener dataOnclickListener;
        TextView product;


        public ViewHolder(@NonNull View itemView, TVAdapter.dataOnclickListener dataOnclickListener) {
            super(itemView);
            product = itemView.findViewById(R.id.textView79);
            this.dataOnclickListener = dataOnclickListener;

            product.setOnClickListener(new View.OnClickListener() {
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




    private String format(String Amount){

        return  "N" + Utils.formatBalance(String.valueOf(Amount));
    }



}
