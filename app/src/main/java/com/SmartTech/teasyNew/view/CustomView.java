package com.SmartTech.teasyNew.view;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.SmartTech.teasyNew.R;

/**
 * Created by muddvayne on 22/03/2017.
 */

public class CustomView extends View {

    private int maxHeight, maxWidth = -1;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
        try {
            String mHeight = a.getString(R.styleable.CustomView_maxHeight);
            if(mHeight != null) {
                maxHeight = dpToPx(mHeight);
            }

            String mWidth = a.getString(R.styleable.CustomView_maxWidth);
            if(mWidth != null) {
                maxWidth = dpToPx(mWidth);
            }
        } finally {
            a.recycle();
        }
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int dpToPx(String value) {
        String str = value.split("dp")[0].split("dip")[0];
        float dp =  Float.valueOf(str);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int)((dp * displayMetrics.density));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            if (maxHeight > 0 && heightSize > maxHeight) {
                heightSize = maxHeight;
            }
            if (maxWidth > 0 && widthSize > maxWidth) {
                widthSize = maxWidth;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
            getLayoutParams().height = heightSize;
            getLayoutParams().width = widthSize;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
