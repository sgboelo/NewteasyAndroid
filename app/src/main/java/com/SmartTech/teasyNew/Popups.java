package com.SmartTech.teasyNew;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by muddvayne on 29/03/2017.
 */

public class Popups {

    public static Dialog showPopup(Activity context, String message, String title) {
        return showPopup(context, message, title, null);
    }

    public static Dialog showPopup(Activity context, String message, String title, final DialogInterface.OnDismissListener onDismiss) {
        final Dialog dialog = new Dialog(context);
        View view = context.getLayoutInflater().inflate(R.layout.popup_default_notification, null);

        ((TextView)view.findViewById(R.id.notification_text)).setText(message);

        if(title != null && title.length() > 0) {
            dialog.setTitle(title);
        }
        else {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        Button buttonOk = (Button) view.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if(onDismiss != null) {
            dialog.setOnDismissListener(onDismiss);
        }

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static Dialog showOperationSuccessDialog(Activity context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = context.getLayoutInflater().inflate(R.layout.popup_operation_success, null);
        view.findViewById(R.id.popup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if(!StringUtils.isBlank(message)) {
            ((TextView)view.findViewById(R.id.popup_message)).setText(message);
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }

    @Deprecated
    /**
     * Use popups.PopupOperationFailed instead
     * */
    public static Dialog showOperationFailDialog(Activity context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = context.getLayoutInflater().inflate(R.layout.popup_operation_fail, null);
        view.findViewById(R.id.popup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if(!StringUtils.isBlank(message)) {
            ((TextView)view.findViewById(R.id.popup_message)).setText(message);
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }

    public static Dialog showOperationInProcessDialog(Activity context, String message) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = context.getLayoutInflater().inflate(R.layout.popup_operation_in_process, null);

        View logo = view.findViewById(R.id.logo_teasy_animated);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.logo);
        logo.startAnimation(anim);

        if(StringUtils.isBlank(message)) {
            view.findViewWithTag("text").setVisibility(View.GONE);
        }
        else {
            view.findViewWithTag("text").setVisibility(View.VISIBLE);
            ((TextView)view.findViewWithTag("text")).setText(message);
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }
}
