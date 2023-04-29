package com.SmartTech.teasyNew;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.activity.MainActivity;

import java.util.Arrays;
import java.util.List;

public class FAQi {
    MainActivity activity;
    private String[] mArray;

    public FAQi(MainActivity activity) {
        this.activity = activity;
    }

    public void FAQ(Context context){
        mArray  = activity.getResources().getStringArray(R.array.FAQs);
        Dialog pinDialog = new Dialog(activity);
        pinDialog.setContentView(R.layout.faq);
        Window window = pinDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        pinDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RecyclerView recyclerView = pinDialog.findViewById(R.id.resclerv);
        Resources RS = activity.getResources();
        List<String> labels = Arrays.asList(RS.getStringArray(R.array.FAQ));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        FAQAdapter myAdapter = new FAQAdapter(labels, new FAQAdapter.dataOnclickListener() {
            @Override
            public void onDATAlister(int position) {
                if(position < mArray.length) {
                    FAQDetails(mArray[position]);
                }

            }
        },activity);


        pinDialog.findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinDialog.dismiss();
            }
        });
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        pinDialog.show();

    }



    public void FAQDetails(String Details){
        Dialog pinDialog = new Dialog(activity);
        pinDialog.setContentView(R.layout.faqdetails);
        Window window = pinDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        pinDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((TextView) pinDialog.findViewById(R.id.textView134)).setText(Details);

        pinDialog.findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinDialog.dismiss();
            }
        });
        pinDialog.show();

    }
}
