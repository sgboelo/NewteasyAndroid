package com.SmartTech.teasyNew.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import com.SmartTech.teasyNew.FontsOverride;
import com.SmartTech.teasyNew.R;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by muddvayne on 04/09/2017.
 */

public class AutoCompleteDropdown extends LinearLayoutCompat {

    private Context context;

    private CustomEditText dropdownInputField;

    private DropdownItem selectedDropdownItem;

    private ScrollView parentScrollView;

    private boolean scrollOnFocus;

    private Typeface font;

    public AutoCompleteDropdown(Context context) {
        super(context);
        this.context = context;
    }

    public AutoCompleteDropdown(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public AutoCompleteDropdown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AutoCompleteDropdown, 0, 0);
        int layoutID = typedArray.getResourceId(R.styleable.AutoCompleteDropdown_layout, R.layout.dropdown);
        inflate(context, layoutID, this);

        dropdownInputField = (CustomEditText) findViewWithTag("dropdown_input_field");
        String dropdownHint = typedArray.getString(R.styleable.AutoCompleteDropdown_hint);
        if(!StringUtils.isBlank(dropdownHint)) {
            dropdownInputField.setHint(dropdownHint);
        }

        int fontID = typedArray.getInt(R.styleable.AutoCompleteDropdown_textFont, -1);
        if (fontID >= 0) {
            this.font = FontsOverride.getFontById(getContext(), fontID);
            dropdownInputField.setFont(font);
        }

        dropdownInputField.setOnClickListener(inputFieldClicked);
        dropdownInputField.addTextChangedListener(dropdownInputTextChanged);

        ViewGroup dropdownItemList = (ViewGroup) findViewWithTag("dropdown_item_list");
        for(int i = 0; i < dropdownItemList.getChildCount(); ++i) {
            DropdownItem item = (DropdownItem) dropdownItemList.getChildAt(i);

            if(this.font != null) {
                item.setFont(this.font);
            }

            addDropdownItem(item);
        }

        boolean enabled = typedArray.getBoolean(R.styleable.AutoCompleteDropdown_enabled, true);
        setEnabled(enabled);

        getInputField().setOnFocusChangeListener(inputFieldFocusChanged);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewParent parent = AutoCompleteDropdown.this;
                do {
                    parent = parent.getParent();
                    if(parent instanceof ScrollView) {
                        parentScrollView = (ScrollView) parent;
                        break;
                    }
                }
                while (parent != null);
            }
        });

        scrollOnFocus = typedArray.getBoolean(R.styleable.AutoCompleteDropdown_scrollOnFocus, true);
        typedArray.recycle();
    }

    public void setHint(String hint) {
        dropdownInputField.setHint(hint);
    }

    private OnFocusChangeListener inputFieldFocusChanged = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                DropdownItem selectedItem = getSelectedDropdownItem();
                if(selectedItem != null) {
                    String text = ((TextView) selectedItem.findViewWithTag("text")).getText().toString();
                    getInputField().setText(text);
                }
                else {
                    getInputField().setText("");
                }
            }
            else {
                //scroll to top of element on focus
                if(scrollOnFocus && parentScrollView != null) {
                    parentScrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View v = AutoCompleteDropdown.this;
                            int y = 0;
                            do {
                                y += v.getTop();
                                v = (View) v.getParent();
                            }
                            while(v != parentScrollView);

                            parentScrollView.scrollTo(0, y);
                        }
                    }, 50);
                }
            }

            if(AutoCompleteDropdown.this.isEnabled()) {
                setDropdownOpened(hasFocus);
            }
        }
    };

    @Nullable
    public DropdownItem getSelectedDropdownItem() {
        return this.selectedDropdownItem;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getInputField().setEnabled(enabled);

        if(!enabled) {
            setDropdownOpened(false);
            getInputField().setText("");
        }
    }

    public void setDropdownOpened(boolean opened) {
        View hint = findViewWithTag("dropdown_hint");

        if(opened) {
            getInputField().setText("");

            findViewWithTag("dropdown_item_list").setVisibility(VISIBLE);

            if(hint != null) {
                hint.setVisibility(VISIBLE);
            }
        }
        else {
            findViewWithTag("dropdown_item_list").setVisibility(GONE);

            if(hint != null) {
                hint.setVisibility(GONE);
            }
        }
    }

    public ViewGroup getItemListContainer() {
        return (ViewGroup) findViewWithTag("dropdown_item_list");
    }

    public CustomEditText getInputField() {
        return dropdownInputField;
    }

    public void addDropdownItem(DropdownItem dropdownItem) {
        addDropdownItem(dropdownItem, false);
    }

    public void addDropdownItem(DropdownItem dropdownItem, boolean selected) {
        //default listener that will hide item list and set it's text to dropdown input field
        dropdownItem.addOnClickListener(
                DropdownItem.OnClickListenerTag.DEFAULT_ONCLICK_LISTENER_TAG.getTag(),
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String value = ((TextView) v.findViewWithTag("text")).getText().toString();
                        getInputField().setText("");
                        getInputField().append(value);

                        setDropdownOpened(false);
                        selectedDropdownItem = (DropdownItem) v;
                    }
                },
                Integer.MIN_VALUE
        );

        ((ViewGroup)findViewWithTag("dropdown_item_list")).addView(dropdownItem);

        if(selected) {
            this.selectedDropdownItem = dropdownItem;
            getInputField().setText(dropdownItem.getText());
            setDropdownOpened(false);
        }

        if(this.font != null) {
            dropdownItem.setFont(font);
        }
    }

    public void removeAllDropdownItems() {
        ((ViewGroup)findViewWithTag("dropdown_item_list")).removeAllViews();
        selectedDropdownItem = null;
        getInputField().setText("");
    }

    private OnClickListener inputFieldClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            setDropdownOpened(true);
        }
    };

    private TextWatcher dropdownInputTextChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            findViewWithTag("dropdown_item_list").setVisibility(VISIBLE);
            String dropdownInputText = s.toString().toLowerCase();

            ViewGroup dropdownItemList = (ViewGroup) findViewWithTag("dropdown_item_list");
            for(int i = 0; i < dropdownItemList.getChildCount(); ++i) {
                DropdownItem item = (DropdownItem) dropdownItemList.getChildAt(i);

                String itemValue = ((TextView)item.findViewWithTag("text"))
                        .getText().toString().toLowerCase();

                if(itemValue.contains(dropdownInputText)) {
                    item.setVisibility(VISIBLE);
                }
                else {
                    item.setVisibility(GONE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
