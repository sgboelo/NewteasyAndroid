package com.SmartTech.teasyNew.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SmartTech.teasyNew.R;

/**
 * Created by muddvayne on 22/06/2017.
 */

public class BottomMenu extends LinearLayout {

    public BottomMenu(Context context) {
        super(context);
    }

    public BottomMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        public void onClick(View v) {
            int childCount = BottomMenu.this.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                View menuItem = getChildAt(i);
                setItemActive(menuItem, false);
            }

            setItemActive(v, true);
        }
    };

    private void setItemActive(View item, boolean active) {
        //update icon
        String tag = item.getTag().toString();
        String iconName = tag + (active ? "_active" : "_inactive");

        int iconResID = getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
        Drawable iconDrawable = getResources().getDrawable(iconResID);

        View iconView = item.findViewWithTag("bottom_menu_item_icon");
        iconView.setBackgroundDrawable( iconDrawable );

        //update text color
        TextView label = (TextView) item.findViewWithTag("bottom_menu_item_label");
        int color = active ? R.color.color_bottom_menu_item_active : R.color.color_bottom_menu_item_inactive;
        label.setTextColor(ContextCompat.getColor(getContext(), color));
    }

}
