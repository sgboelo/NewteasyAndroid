package com.SmartTech.teasyNew;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by muddvayne on 11/03/2017.
 */

public class CustomArrayAdapter extends ArrayAdapter<CharSequence> {

    private Typeface typeface;

    public CustomArrayAdapter(Context context, int resource, CharSequence[] items, Typeface typeface) {
        super(context, resource, items);
        this.typeface = typeface;
    }

    public TextView getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(typeface);
        return view;
    }

    public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(typeface);
        return view;
    }

}
