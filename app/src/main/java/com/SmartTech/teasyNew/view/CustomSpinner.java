package com.SmartTech.teasyNew.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.SmartTech.teasyNew.CustomArrayAdapter;
import com.SmartTech.teasyNew.FontsOverride;
import com.SmartTech.teasyNew.R;

/**
 * Created by muddvayne on 25/04/2017.
 */

public class CustomSpinner extends androidx.appcompat.widget.AppCompatSpinner {

    private Context context;

    private Typeface font;

    public CustomSpinner(Context context) {
        super(context);
        this.context = context;
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
        this.context = context;
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomSpinner, 0, 0);
        font = FontsOverride.getFontById(getContext(), typedArray.getInt(R.styleable.CustomSpinner_textFont, 0));

        final CharSequence[] entries = typedArray.getTextArray(R.styleable.CustomSpinner_values);
        if(entries != null) {
            setupTypeface(entries, font);
        }

        typedArray.recycle();
    }

    public void setEntries(CharSequence[] entries) {
        setupTypeface(entries, font);
    }

    private void setupTypeface(CharSequence[] entries, Typeface font) {
        CustomArrayAdapter adapter = new CustomArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, entries, font);
        setAdapter(adapter);
    }

}
