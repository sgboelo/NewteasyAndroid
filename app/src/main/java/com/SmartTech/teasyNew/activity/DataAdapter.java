package com.SmartTech.teasyNew.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.Utils;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.PADataValidationResponse;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context mContext;
    private List<PADataValidationResponse.Product> products;
    private DataAdapter.dataOnclickListener mdataOnclickListener;

    public DataAdapter(List<PADataValidationResponse.Product> products, DataAdapter.dataOnclickListener mdataOnclickListener,
                       Context mContext) {

        this.mContext = mContext;
        this.products = products;
        this.mdataOnclickListener = mdataOnclickListener;

    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.product_items, viewGroup,false);
        return new DataAdapter.ViewHolder(view,mdataOnclickListener);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.product.setText(products.get(i).displayName());



    }

    @Override
    public int getItemCount() {
        return  products.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        DataAdapter.dataOnclickListener dataOnclickListener;
        TextView product;


        public ViewHolder(@NonNull View itemView, DataAdapter.dataOnclickListener dataOnclickListener) {
            super(itemView);
            product = itemView.findViewById(R.id.textView79);
            this.dataOnclickListener = dataOnclickListener;

            product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataOnclickListener.onDATAlister(products.get(getBindingAdapterPosition()).product_id,
                            products.get(getBindingAdapterPosition()).denomination,
                            products.get(getBindingAdapterPosition()).face_value,
                            products.get(getBindingAdapterPosition()).displayName());
                }
            });

        }


    }

    public interface dataOnclickListener{
        void onDATAlister(String productID, String denomination, String faceValue, String decription);
    }




    private String format(String Amount){

        return  "N" + Utils.formatBalance(String.valueOf(Amount));
    }



}
