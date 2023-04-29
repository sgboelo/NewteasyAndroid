package com.SmartTech.teasyNew;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.SmartTech.teasyNew.activity.MainActivity;

public class TransactionResult {
    private Context context;
    private Dialog confirmkPIN;
    MainActivity activity;
    private TextView Name, AccountNumber, Amount , TransactionCode , Narration , BankName, financialInstitution,
            rescipiantName, recipianAc, AmountHeader, TransactioncodeHeader, NarationHeader, DirectionHeader,Direction, message;
    private ImageView image;

    public TransactionResult(Context context, MainActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void failedTransaction(String msg){
        Dialog confirmkPIN = new Dialog(context);
        confirmkPIN.setContentView(R.layout.transaction_failed_pop);
        Window window = confirmkPIN.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmkPIN.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textView = confirmkPIN.findViewById(R.id.textView39);
        TextView button = confirmkPIN.findViewById(R.id.textView41);
        textView.setText(msg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmkPIN.dismiss();
            }
        });
        confirmkPIN.show();
    }

    public TextView getName() {
        return Name;
    }

    public TextView getAccountNumber() {
        return AccountNumber;
    }

    public TextView getAmount() {
        return Amount;
    }

    public TextView getTransactionCode() {
        return TransactionCode;
    }

    public TextView getNarration() {
        return Narration;
    }

    public TextView getBankName() {
        return BankName;
    }

    public Dialog getConfirmkPIN() {
        return confirmkPIN;
    }

    public TextView getFinancialInstitution() {
        return financialInstitution;
    }

    public TextView getRescipiantName() {
        return rescipiantName;
    }

    public TextView getRecipianAc() {
        return recipianAc;
    }

    public TextView getAmountHeader() {
        return AmountHeader;
    }

    public TextView getTransactioncodeHeader() {
        return TransactioncodeHeader;
    }

    public TextView getNarationHeader() {
        return NarationHeader;
    }

    public TextView getDirectionHeader() {
        return DirectionHeader;
    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(TextView message) {
        this.message = message;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public void setDirectionHeader(TextView directionHeader) {
        DirectionHeader = directionHeader;
    }

    public TextView getDirection() {
        return Direction;
    }

    public void setDirection(TextView direction) {
        Direction = direction;
    }

    public void successfulTransaction(){
       // View mView;
        confirmkPIN = new Dialog(context);
        confirmkPIN.setContentView(R.layout.transaction_successful);
        Window window = confirmkPIN.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        confirmkPIN.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //mView = confirmkPIN.findViewById(R.id.linewrr);
        Name = confirmkPIN.findViewById(R.id.textView51);
        AccountNumber = confirmkPIN.findViewById(R.id.textView53);
        Amount = confirmkPIN.findViewById(R.id.textView55);
        TransactionCode = confirmkPIN.findViewById(R.id.textView49);
        Narration = confirmkPIN.findViewById(R.id.textView47);
        financialInstitution = confirmkPIN.findViewById(R.id.textView61);
        rescipiantName = confirmkPIN.findViewById(R.id.textView46);
        recipianAc = confirmkPIN.findViewById(R.id.textView48);
        AmountHeader = confirmkPIN.findViewById(R.id.textView50);
        TransactioncodeHeader = confirmkPIN.findViewById(R.id.textView54);
        NarationHeader = confirmkPIN.findViewById(R.id.textView52);
        TextView Ok = confirmkPIN.findViewById(R.id.textView59);
        TextView Share = confirmkPIN.findViewById(R.id.textView58);
        BankName = confirmkPIN.findViewById(R.id.textView60);
        DirectionHeader = confirmkPIN.findViewById(R.id.textView136);
        Direction = confirmkPIN.findViewById(R.id.textView137);
        message = confirmkPIN.findViewById(R.id.textView56);
        image = confirmkPIN.findViewById(R.id.imageView5);

        //mView = window.getDecorView();
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmkPIN.dismiss();
            }
        });
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ScreenShot screenShot = new ScreenShot(activity);
                screenShot.takeScreenshot(confirmkPIN);
                confirmkPIN.dismiss();
            }
        });

    }
}
