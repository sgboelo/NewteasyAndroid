package com.SmartTech.teasyNew.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

/**
 * Created by muddvayne on 02/03/2017.
 */

public class CustomDatePicker extends DatePicker {
    public CustomDatePicker(Context context) {
        super(context);
    }

    public CustomDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        ViewParent parent = getParent();

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        return false;
    }
}
