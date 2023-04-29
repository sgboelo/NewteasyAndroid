package com.SmartTech.teasyNew.popups;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.SmartTech.teasyNew.R;

/**
 * Created by muddvayne on 08/01/2018.
 */

public class PopupYesNoQuestion extends Dialog {

    private TextView btnYes, btnNo;

    private TextView questionText;

    public PopupYesNoQuestion(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        View layout = getLayoutInflater().inflate(R.layout.popup_yes_no_question, null);
        setContentView(layout);

        questionText = (TextView) findViewById(R.id.question_text);
        btnYes = (TextView) layout.findViewById(R.id.btn_yes);
        btnNo = (TextView) layout.findViewById(R.id.btn_no);

        btnNo.setOnClickListener(defaultOnClickNo);
    }

    private View.OnClickListener defaultOnClickNo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupYesNoQuestion.this.dismiss();
        }
    };

    public void setYesClicked(View.OnClickListener yesClicked) {
        this.btnYes.setOnClickListener(yesClicked);
    }

    public void setNoClicked(View.OnClickListener noClicked) {
        this.btnNo.setOnClickListener(noClicked);
    }

    public void setQuestionText(String text) {
        questionText.setText(text);
    }
}
