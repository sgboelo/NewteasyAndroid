package com.SmartTech.teasyNew.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.SmartTech.teasyNew.FontsOverride;
import com.SmartTech.teasyNew.R;

import static com.SmartTech.teasyNew.view.DropdownItem.OnClickListenerTag.MAIN_ONCLICK_LISTENER_TAG;

/**
 * Created by muddvayne on 04/09/2017.
 */

public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {

    private CompositeOnClickListener compositeOnClickListener = new CompositeOnClickListener();

    private Typeface font;

    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.customEditTextStyle);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    protected void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.CustomEditText, defStyleAttr, R.style.CustomEditText
        );
        int fontID = typedArray.getInt(R.styleable.CustomEditText_textFont, -1);

        if (fontID >= 0) {
            font = FontsOverride.getFontById(getContext(), typedArray.getInt(R.styleable.CustomEditText_textFont, 0));
            setTypeface(font);
        }
    }

    public void setFont(Typeface font) {
        this.font = font;
        setTypeface(font);
    }

    /**
     * this View actually uses composite CompositeOnClickListener which contains multiple listeners
     * this method will override the listener that was added by itself
     * if you want to add another listener, use addOnClickListener instead
     *
     * this listener will be tagged by OnClickListenerTag.MAIN_ONCLICK_LISTENER_TAG.getTag()
     * you can use this tag later to remove this listener from the list
     * you can also pass null to this method to remove listeners with this tag
     *
     * this listener will get 0 order value
     * */
    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        if(listener == null) {
            this.compositeOnClickListener.removeAllListenersWithTag(MAIN_ONCLICK_LISTENER_TAG.getTag());
            return;
        }

        this.compositeOnClickListener.removeAllListenersWithTag(MAIN_ONCLICK_LISTENER_TAG.getTag());
        this.compositeOnClickListener.addListener(MAIN_ONCLICK_LISTENER_TAG.getTag(), listener, 0);
        super.setOnClickListener(compositeOnClickListener);
    }

    /**
     * adds listener to the queue. all listeners will be executed in specified order
     * @param tag you can use it later to remove all listeners from the queue with same tag
     * @param order listeners will be executed one by one.
     *              listeners with smaller order will be executed first.
     * */
    public void addOnClickListener(String tag, OnClickListener listener, int order) {
        this.compositeOnClickListener.addListener(tag, listener, order);
        super.setOnClickListener(compositeOnClickListener);
    }

    /**
     * adds listener to the queue. all listeners will be executed in specified order
     * @param tag you can use it later to remove all listeners from the queue with same tag
     *
     * listener will get default execution order
     * */
    public void addOnClickListener(String tag, OnClickListener listener) {
        this.compositeOnClickListener.addListener(tag, listener);
        super.setOnClickListener(compositeOnClickListener);
    }

    /**
     * adds listener to the queue. all listeners will be executed in specified order
     * listener will be tagged by empty string and will get default execution order
     * */
    public void addOnClickListener(OnClickListener listener) {
        this.compositeOnClickListener.addListener(listener);
        super.setOnClickListener(compositeOnClickListener);
    }

}
