package com.SmartTech.teasyNew.activity;


import android.content.Context;
import android.graphics.Color;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.Utils;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.TransactionHistoryResponse;
import com.SmartTech.teasyNew.model.historyBankTransferModel;

import java.util.List;
import java.util.Map;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.ViewHolder> {

    private List<TransactionHistoryResponse.TransactionHistoryEntry> mdata;
    private Context mContext;

    private historyAdapter.transactionOnclickLintener mtransactionOnclickLintener;


    public historyAdapter(List<TransactionHistoryResponse.TransactionHistoryEntry>  mdata, Context mContext, historyAdapter.transactionOnclickLintener mtransactionOnclickLintener) {
        this.mdata = mdata;
        this.mContext = mContext;
        this.mtransactionOnclickLintener = mtransactionOnclickLintener;

    }

    @NonNull
    @Override
    public historyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.transction_history_items, viewGroup,false);
        return new historyAdapter.ViewHolder(view, mtransactionOnclickLintener);
    }

    @Override
    public void onBindViewHolder(historyAdapter.ViewHolder viewHolder, int i) {

        if(mdata.get(i).reverted){
            viewHolder.status.setText("Failed");
        }else{
            viewHolder.status.setText("Success");
        }
        if(mdata.size()-1 == i){
            viewHolder.line.setVisibility(View.GONE);
        }
        if(mdata.get(i).transactionType.equals("MCashIn")){
            viewHolder.type.setText("Credit");
            viewHolder.type.setTextColor(Color.GREEN);
        } else {
            if (mdata.get(i).reverted) {
                if (mdata.get(i).reverted) {
                    viewHolder.type.setText("Debit");
                    viewHolder.type.setTextColor(Color.RED);


                }
            }else{
                switch (mdata.get(i).transactionType) {
                    case "Admin send money (no fee)":
                    case "Send money OTC":
                    case "Send money":
                    case "Deposit money":
                    case "Cash In":
                    case "MCashIn OTC":
                    case "Fees to wallet":
                    case "Reverse transaction":
                        viewHolder.type.setText("Credit");
                        viewHolder.type.setTextColor(Color.GREEN);

                        break;
                    default:
                        viewHolder.type.setText("Debit");
                        viewHolder.type.setTextColor(Color.RED);

                        break;
                }
            }
        }
        if(mdata.get(i).transactionType.equals("Bank Transfer")){
               historyBankTransferModel transferModel = new historyBankTransferModel();
               String Name = mdata.get(i).additionalData.get(transferModel.getTargetAccountName());
               String code = mdata.get(i).additionalData.get(transferModel.getDestinationCode());
               String bvn = mdata.get(i).additionalData.get(transferModel.getBvn());
               String sourceName = mdata.get(i).additionalData.get(transferModel.getSourceAccountName());
               transferModel.setBvn(bvn);
               transferModel.setDestinationCode(code);
               transferModel.setTargetAccountName(Name);
               transferModel.setSourceAccountName(sourceName);


        }

        if (mdata.get(i).destinationWallet.equals("11120000001 - AEDC")) {
            viewHolder.Name.setText("AEDC Purchase");
        }else{
            viewHolder.Name.setText(mdata.get(i).transactionType);
        }
        viewHolder.Amount.setText(format(mdata.get(i).amount));
        String id = "ID: "+ mdata.get(i).id;
        viewHolder.account.setText(id);
        viewHolder.timeAndDate.setText(mdata.get(i).timestamp);

        hideValues(viewHolder.Amount);




    }

    @Override
    public int getItemCount() {
        return  mdata.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        historyAdapter.transactionOnclickLintener transactionOnclickLintener;
        LinearLayout cardView;
        TextView timeAndDate,Name,account,Amount,type, status;

        View line;

        public ViewHolder(@NonNull View itemView, historyAdapter.transactionOnclickLintener transactionOnclickLintener ) {
            super(itemView);

            timeAndDate = itemView.findViewById(R.id.textView);
            Name = itemView.findViewById(R.id.textView19);
            Amount = itemView.findViewById(R.id.textView22);
            account = itemView.findViewById(R.id.textView20);
            cardView = itemView.findViewById(R.id.mcard);
            type = itemView.findViewById(R.id.textView135);
            line = itemView.findViewById(R.id.view2);
            status = itemView.findViewById(R.id.textView72);
           this.transactionOnclickLintener = transactionOnclickLintener;

           cardView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   transactionOnclickLintener.onTransactionListener(getBindingAdapterPosition(),mdata.get(getBindingAdapterPosition()).transactionType,
                           mdata.get(getBindingAdapterPosition()).amount, mdata.get(getBindingAdapterPosition()).id,
                           mdata.get(getBindingAdapterPosition()).timestamp,mdata.get(getBindingAdapterPosition()).details,
                           mdata.get(getBindingAdapterPosition()).reverted, mdata.get(getBindingAdapterPosition()).initiatorWallet,
                           mdata.get(getBindingAdapterPosition()).destinationWallet,mdata.get(getBindingAdapterPosition()).additionalDestNumber,
                           mdata.get(getBindingAdapterPosition()).fees,  mdata.get(getBindingAdapterPosition()).balanceBefore,
                           mdata.get(getBindingAdapterPosition()).balanceAfter, mdata.get(getBindingAdapterPosition()).additionalData);
               }
           });

        }


    }

    public interface transactionOnclickLintener{
        void onTransactionListener(int position, String Type , String Amount, String id,
                                   String timeDate, Map<String, String> details,Boolean reverted,
                                   String initiatorWallet,String DestinationWallet, String AdditionalWallet,
                                   String fee, String balanceBefore, String balanceAfter, Map<String, String> deta);
    }

    private String format(String Amount){

        return  "₦" + Utils.formatBalance(String.valueOf(Amount));
    }


    private void hideValues(TextView textView){
        if(new database(mContext).getmStatus() == 1) {
            textView.setTransformationMethod(new PasswordTransformationMethod());

            new database(mContext).insertToggle(1,1);
        }else{
            textView.setTransformationMethod(null);
            new database(mContext).insertToggle(1,0);

        }
    }

}
