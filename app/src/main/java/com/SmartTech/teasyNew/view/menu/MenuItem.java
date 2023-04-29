package com.SmartTech.teasyNew.view.menu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by muddvayne on 04/04/2017.
 */

public class MenuItem extends LinearLayout {

    private CompositeOnClickListener onClickListener;

    public MenuItem(Context context) {
        super(context);
    }

    public MenuItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if(onClickListener == null) {
            onClickListener = new CompositeOnClickListener();
        }

        onClickListener.registerListener(l);
        super.setOnClickListener(onClickListener);
    }

    private class CompositeOnClickListener implements View.OnClickListener {

        private Set<OnClickListener> registeredListeners = new HashSet<>();

        public void registerListener (View.OnClickListener listener) {
            registeredListeners.add(listener);
        }

        public void unregisterListener(View.OnClickListener listener) {
            registeredListeners.remove(listener);
        }

        @Override
        public void onClick(View view) {
            for(View.OnClickListener listener : registeredListeners)  {
                listener.onClick(view);
            }
        }

    }
}
