package com.SmartTech.teasyNew.popups;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.SmartTech.teasyNew.R;

import org.apache.commons.lang3.StringUtils;

public class PopupOperationInProcess extends Dialog {

    private View logo;
    private TextView popupText;

    public PopupOperationInProcess(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        View layout = getLayoutInflater().inflate(R.layout.popup_operation_in_process, null);
        setContentView(layout);

        logo = layout.findViewById(R.id.logo_teasy_animated);
        popupText = layout.findViewWithTag("text");
        popupText.setVisibility(View.GONE);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void setPopupText(String text) {
        if(StringUtils.isBlank(text)) {
            popupText.setText("");
            popupText.setVisibility(View.GONE);
        }
        else {
            popupText.setText(text);
            popupText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        super.show();

        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.logo);
        logo.startAnimation(anim);
    }
}
