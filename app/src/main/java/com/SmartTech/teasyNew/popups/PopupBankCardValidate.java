package com.SmartTech.teasyNew.popups;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.SmartTech.teasyNew.R;

/**
 * Created by muddvayne on 29/12/2017.
 */

public class PopupBankCardValidate extends Dialog {

    private EditText fieldAmount;
    private TextView popupButton;

    public PopupBankCardValidate(@NonNull final Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);

        final View layout = getLayoutInflater().inflate(R.layout.popup_confirm_bank_card, null);
        popupButton = (TextView) layout.findViewWithTag("popup_button");
        fieldAmount = (EditText) layout.findViewWithTag("amount_input_field");
        fieldAmount.requestFocus();

        setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                fieldAmount.requestFocus();

                try {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) context
                                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputMethodManager.showSoftInput(fieldAmount, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }, 100);
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupBankCardValidate.this.dismiss();
            }
        });

        setContentView(layout);
    }

    public String getAmount() {
        return fieldAmount.getText().toString();
    }

    public void setButtonOnClickListener(View.OnClickListener listener) {
        this.popupButton.setOnClickListener(listener);
    }
}
