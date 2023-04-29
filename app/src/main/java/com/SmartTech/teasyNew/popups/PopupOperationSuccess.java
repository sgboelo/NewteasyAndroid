package com.SmartTech.teasyNew.popups;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.view.CompositeOnClickListener;

/**
 * Created by muddvayne on 29/12/2017.
 */

public class PopupOperationSuccess extends Dialog {

    private TextView button;
    private TextView message;
    private CompositeOnClickListener buttonOnClickListener = new CompositeOnClickListener();

    public PopupOperationSuccess(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);

        View layout = getLayoutInflater().inflate(R.layout.popup_operation_success, null);
        setContentView(layout);

        button = (TextView) layout.findViewById(R.id.popup_button);
        message = (TextView) layout.findViewById(R.id.popup_message);

        buttonOnClickListener.addListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupOperationSuccess.this.dismiss();
            }
        });

        button.setOnClickListener(buttonOnClickListener);
    }

    public void setButtonText(String text) {
        button.setText(text);
    }

    public void setButtonOnClickListener(View.OnClickListener listener) {
        button.setOnClickListener(listener);
    }

    public void addButtonOnClickListener(View.OnClickListener listener) {
        buttonOnClickListener.addListener(listener);
    }

    public void setText(String text) {
        message.setText(text);
    }
}
