package com.SmartTech.teasyNew.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.view.AmountInputField;

public class enter_amount {
    private MainActivity activity;
    private AmountInputField mAmount;
    Dialog myDialog;

    public enter_amount(MainActivity activity) {
        this.activity = activity;
    }

    public Button enterAmount(){
        myDialog = new Dialog(activity);
        myDialog.setContentView(R.layout.biller_amount);
        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        wlp.gravity = Gravity.CENTER_HORIZONTAL;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().getAttributes().windowAnimations = R.anim.bottom_up;
        mAmount = myDialog.findViewById(R.id.amountInputField3);
        Button OkBTN =myDialog.findViewById(R.id.button2);

        myDialog.show();
        return OkBTN;
    }

    public AmountInputField getmAmount() {
        return mAmount;
    }

    public Dialog getMyDialog() {
        return myDialog;
    }
}
