package com.SmartTech.teasyNew.view;

import android.content.Context;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.SmartTech.teasyNew.model.BankCard;

/**
 * Created by muddvayne on 28/12/2017.
 */

public class BankCardFormView extends LinearLayoutCompat {

    private static final String CARD_NUMBER_FIELD_TAG_PATTERN = "card_number_field_%d";
    private ViewGroup cardRootViewGroup;

    public BankCardFormView(Context context) {
        super(context);
        init();
    }

    public BankCardFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BankCardFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public String getCardNumber() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 4; ++i) {
            int fieldID = i + 1;

            String tag = String.format(CARD_NUMBER_FIELD_TAG_PATTERN, fieldID);
            EditText cardNumberInputField = (EditText) cardRootViewGroup.findViewWithTag(tag);

            sb.append(cardNumberInputField.getText());
        }

        return sb.toString();
    }

    public String getExpiryMonth() {
        EditText field = (EditText) findViewWithTag("field_expiry_month");
        return field.getText().toString();
    }

    public String getExpiryYear() {
        EditText field = (EditText) findViewWithTag("field_expiry_year");
        return field.getText().toString();
    }

    private void init() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initCardNumberFields();
                initExpiryMonthField();
                initExpiryYearField();
            }
        });
    }

    private void initExpiryYearField() {
        final EditText fieldExpiryMonth =
                (EditText) cardRootViewGroup.findViewWithTag("field_expiry_month");
        final EditText fieldExpiryYear =
                (EditText) cardRootViewGroup.findViewWithTag("field_expiry_year");

        fieldExpiryYear.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(fieldExpiryYear.getSelectionStart() == 0) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        fieldExpiryMonth.requestFocus();
                        fieldExpiryMonth.setSelection(fieldExpiryMonth.length());
                        return true;
                    }
                }

                return false;
            }
        });
    }

    private void initExpiryMonthField() {
        final EditText fieldExpiryMonth =
                (EditText) cardRootViewGroup.findViewWithTag("field_expiry_month");
        final EditText fieldExpiryYear =
                (EditText) cardRootViewGroup.findViewWithTag("field_expiry_year");

        fieldExpiryMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2) {
                    fieldExpiryYear.requestFocus();
                    fieldExpiryYear.setSelection(fieldExpiryYear.length());
                }

                if(s.length() > 2) {
                    CharSequence part1 = s.subSequence(0, 2);
                    CharSequence part2 = s.subSequence(2, s.length());

                    s.clear();
                    s.append(part1);

                    fieldExpiryYear.getText().insert(0, part2);
                    fieldExpiryYear.requestFocus();
                }
            }
        });
    }

    private void initCardNumberFields() {
        cardRootViewGroup = (ViewGroup) findViewWithTag("card_root");
        for(int i = 0; i < 4; ++i) {
            int fieldID = i + 1;

            String tag = String.format(CARD_NUMBER_FIELD_TAG_PATTERN, fieldID);
            EditText cardNumberInputField = (EditText) cardRootViewGroup.findViewWithTag(tag);

            cardNumberInputField.addTextChangedListener(
                    new CardNumberFieldWatcher(fieldID)
            );
            cardNumberInputField.setOnKeyListener(
                    new CardNumberFieldKeyListener(cardNumberInputField, fieldID)
            );
            cardNumberInputField.setOnFocusChangeListener(
                    new CardNumberFocusListener(cardNumberInputField, fieldID)
            );
        }
    }

    private class CardNumberFocusListener implements OnFocusChangeListener {

        private int id;

        private EditText field;

        private CardNumberFocusListener(EditText field, int id) {
            this.id = id;
            this.field = field;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(id == 1) {
                return;
            }

            if(hasFocus && field.length() == 0) {
                //if previous field is empty, focus it
                String tag = String.format(CARD_NUMBER_FIELD_TAG_PATTERN, id - 1);
                EditText prevField = (EditText) cardRootViewGroup.findViewWithTag(tag);

                if(prevField.length() < 4) {
                    prevField.requestFocus();

                    try {
                        InputMethodManager inputMethodManager =
                                (InputMethodManager)BankCardFormView.this.getContext()
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputMethodManager.showSoftInput(prevField, InputMethodManager.SHOW_IMPLICIT);
                    }
                    catch (NullPointerException e) {
                        //do nothing
                    }
                }
            }
        }
    }

    private class CardNumberFieldKeyListener implements View.OnKeyListener {

        private int id;

        private EditText field;

        private CardNumberFieldKeyListener(EditText field, int id) {
            this.id = id;
            this.field = field;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(id == 1) {
                return false;
            }

            if(field.getSelectionStart() == 0) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    String tag = String.format(CARD_NUMBER_FIELD_TAG_PATTERN, id - 1);
                    EditText prevField = (EditText) cardRootViewGroup.findViewWithTag(tag);

                    prevField.requestFocus();
                    prevField.setSelection(prevField.length());
                    return true;
                }
            }

            return false;
        }
    }

    private class CardNumberFieldWatcher implements TextWatcher {

        private int id;

        private CardNumberFieldWatcher(int id) {
            this.id = id;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(id == 4) {
                if(s.length() == 7) {
                    focusExpiryMonthField();
                    return;
                }
            }
            else {
                //focus next field
                focusField(id + 1);
            }

            if(s.length() == 0) {
                if(id == 1) {
                    return;
                }

                //focus previous field
                focusField(id - 1);
            }

            hideLogos();
            BankCard.Brand brand = BankCard.Brand.byCardNumber(getCardNumber());
            switch (brand) {
                case VISA:
                    findViewWithTag("icon_visa").setVisibility(VISIBLE);
                    break;

                case MASTERCARD:
                    findViewWithTag("icon_mastercard").setVisibility(VISIBLE);
                    break;

                case AMERICAN_EXPRESS:
                    findViewWithTag("icon_american_express").setVisibility(VISIBLE);
                    break;

                case DISCOVER:
                    findViewWithTag("icon_discover").setVisibility(VISIBLE);
                    break;

                case DINERS_CLUB_AND_CARTE_BLANCHE:
                    findViewWithTag("icon_diners_club").setVisibility(VISIBLE);
                    findViewWithTag("icon_carte_blanche").setVisibility(VISIBLE);
                    break;
            }
        }

        private void hideLogos() {
            findViewWithTag("icon_visa").setVisibility(GONE);
            findViewWithTag("icon_mastercard").setVisibility(GONE);
            findViewWithTag("icon_american_express").setVisibility(GONE);
            findViewWithTag("icon_discover").setVisibility(GONE);
            findViewWithTag("icon_diners_club").setVisibility(GONE);
            findViewWithTag("icon_carte_blanche").setVisibility(GONE);
        }

        private void focusField(int id) {
            String tag = String.format(CARD_NUMBER_FIELD_TAG_PATTERN, id);
            EditText field = (EditText) cardRootViewGroup.findViewWithTag(tag);

            field.requestFocus();
            field.setSelection(field.length());
        }

        private void focusExpiryMonthField() {
            EditText field = (EditText) cardRootViewGroup.findViewWithTag("field_expiry_month");
            field.requestFocus();
            field.setSelection(field.length());
        }
    }
}
