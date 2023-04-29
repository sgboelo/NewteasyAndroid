package com.SmartTech.teasyNew.view;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import static com.SmartTech.teasyNew.view.DropdownItem.OnClickListenerTag.MAIN_ONCLICK_LISTENER_TAG;

/**
 * Created by muddvayne on 04/09/2017.
 */

public class DropdownItem extends LinearLayout {

    private CompositeOnClickListener compositeOnClickListener = new CompositeOnClickListener();

    private Map<String, String> data = new HashMap<>();

    public DropdownItem(Context context) {
        super(context);
    }

    public DropdownItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DropdownItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * this View actually uses composite CompositeOnClickListener which contains multiple listeners
     * this method will override the listener that was added by itself
     * if you want to add another listener, use addOnClickListener instead
     *
     * this listener will be tagged by OnClickListenerTag.MAIN_ONCLICK_LISTENER_TAG.getTag()
     * you can use this tag later to remove this listener from the list
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

    public void setText(String text) {
        ((TextView)findViewWithTag("text")).setText(text);
    }

    public String getText() {
        return ((TextView)findViewWithTag("text")).getText().toString();
    }

    public void setFont(Typeface font) {
        ((TextView)findViewWithTag("text")).setTypeface(font);
    }

    public void putData(String key, String value) {
        this.data.put(key, value);
    }

    public String getData(String key) {
        return data.get(key);
    }

    /**
     * @return CompositeOnClickListener which may contain multiple listeners
     * which will be executed all one by one.
     * */
    public CompositeOnClickListener getOnClickListener() {
        if(this.compositeOnClickListener == null) {
            this.compositeOnClickListener = new CompositeOnClickListener();
        }
        return this.compositeOnClickListener;
    }

    public enum OnClickListenerTag {
        /**
         * default listener that hides item list and sets it's text to dropdown input field
         * */
        DEFAULT_ONCLICK_LISTENER_TAG("dropdown_item_default_listener"),

        /**
         * listener that was set in xml or via first call of DropdownItem.setOnClickListener
         * */
        MAIN_ONCLICK_LISTENER_TAG("dropdown_item_main_listener");

        /**
         * use the tag to remove all listeners with same tags from CompositeOnClickListener
         * */
        private String tag;

        OnClickListenerTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return this.tag;
        }
    }
}
