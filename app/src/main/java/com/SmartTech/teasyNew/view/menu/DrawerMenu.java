package com.SmartTech.teasyNew.view.menu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by muddvayne on 04/04/2017.
 */

public class DrawerMenu extends LinearLayout {
    public DrawerMenu(Context context) {
        super(context);
    }

    public DrawerMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawerMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setOnClickListener(onMenuItemClick);
        }
    }

    private OnClickListener onMenuItemClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //set item selected
            int childCount = DrawerMenu.this.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                View menuItem = getChildAt(i);
                menuItem.setSelected(false);
            }
            view.setSelected(true);

            //close drawer
            DrawerLayout drawerLayout = getDrawerLayout(view);
            if(drawerLayout == null) {
                throw new IllegalStateException("Can't find drawer layout");
            }
            else {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        }
    };

    private DrawerLayout getDrawerLayout(View view) {
        View parent = (View) view.getParent();
        if (parent == null) {
            return null;
        }

        if(parent instanceof DrawerLayout) {
            return (DrawerLayout) parent;
        }

        return getDrawerLayout(parent);
    }


}
