package com.SmartTech.teasyNew.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.SmartTech.teasyNew.R;

public class privacy {
    private Context context;
    Dialog myDialog;
    Button cancel, accept;

    public privacy(Context context) {
        this.context = context;
    }

    public Dialog getMyDialog() {
        return myDialog;
    }

    public Button getCancel() {
        return cancel;
    }

    public Button getAccept() {
        return accept;
    }

    public void privacyPolicy(){
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.privacy_policy);
        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        cancel = myDialog.findViewById(R.id.cancel);
        accept = myDialog.findViewById(R.id.button6);

        //support


    }
}
