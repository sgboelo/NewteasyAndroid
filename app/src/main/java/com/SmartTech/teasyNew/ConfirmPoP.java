package com.SmartTech.teasyNew;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class ConfirmPoP {

    Activity activity;

    public ConfirmPoP(Activity activity) {
        this.activity = activity;
    }

    public Dialog confirm(){
        Dialog quickDialog = new Dialog(activity);
        quickDialog.setContentView(R.layout.confirm_payment);
        Window window = quickDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        quickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return quickDialog;
    }
}
