package com.SmartTech.teasyNew.activity;


import android.annotation.SuppressLint;
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
import com.SmartTech.teasyNew.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> notification;
    NotificationAdapter.notificationOnClickLintener mnotificationOnClickLintener;


    public NotificationAdapter(List<Notification> notification, Context mContext, NotificationAdapter.notificationOnClickLintener mnotificationOnClickLintener) {

        this.mnotificationOnClickLintener = mnotificationOnClickLintener;
        this.mContext = mContext;
        this.notification = notification;

    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.transction_history_items, viewGroup,false);
        return new NotificationAdapter.ViewHolder(view,  mnotificationOnClickLintener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder viewHolder, int i) {

        if(String.valueOf(notification.get(i).getType()).equals("AIRTIME_PURCHASE")){
            viewHolder.Name.setText("Airtime/Data");
        }else {
            viewHolder.Name.setText(String.valueOf(notification.get(i).getType()));
        }
        viewHolder.Amount.setText(format(String.valueOf(notification.get(i).amount)));
        //String id = "ID: "+ notification.get(i).getStatus();
        viewHolder.account.setText(String.valueOf(notification.get(i).getStatus()));
        viewHolder.timeAndDate.setText(String.valueOf(notification.get(i).created));

        if(String.valueOf(notification.get(i).direction).equals("1")){
            viewHolder.type.setTextColor(Color.GREEN);
            viewHolder.type.setText("Credit");
        }else {
            viewHolder.type.setTextColor(Color.RED);
            viewHolder.type.setText("Debit");
        }
        hideValues(viewHolder.Amount);
    }

    @Override
    public int getItemCount() {
        return  notification.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        NotificationAdapter.notificationOnClickLintener notificationOnClickLintener;
        LinearLayout cardView;
        TextView timeAndDate,Name,account,Amount,type;


        public ViewHolder(@NonNull View itemView, NotificationAdapter.notificationOnClickLintener notificationOnClickLintener) {
            super(itemView);

            timeAndDate = itemView.findViewById(R.id.textView);
            Name = itemView.findViewById(R.id.textView19);
            Amount = itemView.findViewById(R.id.textView22);
            account = itemView.findViewById(R.id.textView20);
            cardView = itemView.findViewById(R.id.mcard);
            type = itemView.findViewById(R.id.textView135);
            this.notificationOnClickLintener = notificationOnClickLintener;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationOnClickLintener.onNotificationListener(
                            String.valueOf(notification.get(getBindingAdapterPosition()).created),
                            notification.get(getBindingAdapterPosition()).amount,
                            String.valueOf(notification.get(getBindingAdapterPosition()).getStatus()),
                            notification.get(getBindingAdapterPosition()).remarks,
                            String.valueOf(notification.get(getBindingAdapterPosition()).getType())
                    , notification.get(getBindingAdapterPosition()).direction);
                }
            });

        }


    }
    public interface notificationOnClickLintener{
        void onNotificationListener(String Date, long Amount,String Status, String Remark, String Type, int Direction);
    }

    private String format(String Amount){

        return  "₦" + Utils.formatBalance(Amount);
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
