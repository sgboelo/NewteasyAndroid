package com.SmartTech.teasyNew.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.SmartTech.teasyNew.R;

public class ExitApp {
    Context context;
    Button Yes, No;
    Dialog pinDialog;
    public ExitApp(Context context) {
        this.context = context;
    }

    public Button getYes() {
        return Yes;
    }

    public void setYes(Button yes) {
        Yes = yes;
    }

    public Button getNo() {
        return No;
    }

    public void setNo(Button no) {
        No = no;
    }

    public Dialog getPinDialog() {
        return pinDialog;
    }

    public void Exit(String msg){

        pinDialog = new Dialog(context);
        pinDialog.setContentView(R.layout.popup_exit_app);
        Window window = pinDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        pinDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Yes = pinDialog.findViewById(R.id.dialog_yes);
        No = pinDialog.findViewById(R.id.dialog_no);
        TextView textView = pinDialog.findViewById(R.id.popup_send_data_header);
        textView.setText(msg);



    }
}
