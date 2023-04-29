package com.SmartTech.teasyNew.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.SmartTech.teasyNew.FontsOverride;
import com.SmartTech.teasyNew.R;

/**
 * Created by muddvayne on 04/04/2017.
 */

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0);
        Typeface font = FontsOverride.getFontById(getContext(), typedArray.getInt(R.styleable.CustomTextView_textFont, 0));

        setTypeface(font);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
