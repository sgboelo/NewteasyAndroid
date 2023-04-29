package com.SmartTech.teasyNew.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by muddvayne on 08/12/2017.
 */

public class AmountInputField extends CustomEditText implements Serializable {

    private StringBuilder value = new StringBuilder("");

    private String valuePrefix = "";

    public AmountInputField(Context context) {
        this(context, null);
    }

    public AmountInputField(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.customEditTextStyle);
    }

    public AmountInputField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AmountInputField);
        String prefix = ta.getString(R.styleable.AmountInputField_valuePrefix);
        if(!StringUtils.isBlank(prefix)) {
            this.valuePrefix = prefix;
        }
        ta.recycle();

        addTextChangedListener(watcher);
    }

    public long getValueInCents() {
        if(value.length() < 1) {
            return 0l;
        }
        long mvalues = Long.parseLong(value.toString());
        return mvalues;
    }

    public void setValue(long value) {
        this.value = new StringBuilder(String.valueOf(value));

        String amountFormatted = valuePrefix + Utils.formatBalance(value);
        setText(amountFormatted);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        boolean b =  super.requestFocus(direction, previouslyFocusedRect);
        if(b) {
            setSelection(getText().length()); //move cursor to end
        }

        return b;
    }

    public void reset() {
        removeTextChangedListener(watcher);

        this.value.setLength(0);
        setText("");

        addTextChangedListener(watcher);
    }

    TextWatcher watcher = new TextWatcher() {

        private boolean needUpdate = false;

        private String getPlaceholder() {
            return valuePrefix + "0.00";
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(count > 0) {
                String inputValue = s.subSequence(start, start+count).toString();
                if(value.length() < 1 && inputValue.startsWith("0")) {
                    if(!getPlaceholder().equals(inputValue)) {
                        needUpdate = true;
                    }
                }
                else if(inputValue.matches("[0-9]+")) {
                    value.append(s.subSequence(start, start + count));
                    needUpdate = true;
                }
            }

            if(count == 0 && before > 0 && value.length() > 0) {
                value.delete(value.length() - before, value.length());
                needUpdate = true;
            }

            if(count == 0 && before > 0 && s.length() < getPlaceholder().length()) {
                needUpdate = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(needUpdate) {
                needUpdate = false;

                String displayValue;
                if(value.length() > 9) {
                    value = new StringBuilder("999999999");
                    displayValue = valuePrefix + Utils.formatBalance(value.toString());
                }
                else if(value.length() > 0) {
                    displayValue = valuePrefix + Utils.formatBalance(value.toString());
                }
                else {
                    displayValue = getPlaceholder();
                }

                AmountInputField.this.setText(displayValue);
                AmountInputField.this.setSelection( //move cursor to end
                        AmountInputField.this.getText().length()
                );
            }
        }
    };
}
