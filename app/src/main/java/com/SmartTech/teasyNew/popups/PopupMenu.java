package com.SmartTech.teasyNew.popups;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.SmartTech.teasyNew.R;

/**
 * Created by muddvayne on 09/01/2018.
 */

public class PopupMenu extends Dialog {

    private ViewGroup elementsContainer;

    public PopupMenu(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View root = getLayoutInflater().inflate(R.layout.popup_menu, null);
        setContentView(root);

        if(root.getId() == R.id.element_container) {
            elementsContainer = (ViewGroup) root;
        }
        else {
            elementsContainer = (ViewGroup) root.findViewById(R.id.element_container);
        }

        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().setBackgroundDrawable(null);
    }

    public Element addMenuElement() {
        return addMenuElement(R.layout.popup_menu_element);
    }

    public Element addMenuElement(@LayoutRes int layout) {
        Element element = new Element(layout);
        elementsContainer.addView(element);

        return element;
    }

    public class Element extends RelativeLayout {

        private TextView elementText;

        private Element(@LayoutRes int layout) {
            super(PopupMenu.this.getContext());
            inflate(getContext(), layout, this);

            this.elementText = (TextView) this.findViewWithTag("popup_menu_element_text");
        }

        public void setText(String text) {
            this.elementText.setText(text);
        }
    }
}
