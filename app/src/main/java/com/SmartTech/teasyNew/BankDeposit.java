package com.SmartTech.teasyNew;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.SmartTech.teasyNew.activity.MainActivity;

public class BankDeposit {
    MainActivity activity;

    public BankDeposit(MainActivity activity) {
        this.activity = activity;
    }

    public void bankSelection(){
        Dialog mDialog = new Dialog(activity);
        mDialog.setContentView(R.layout.bank_deposit);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((LinearLayout)mDialog.findViewById(R.id.gtBank)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GTB_();
            }
        });

        ((ImageButton)mDialog.findViewById(R.id.imageButton8)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        ((LinearLayout)mDialog.findViewById(R.id.zenith)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Zenitb_();
            }
        });

        ((LinearLayout)mDialog.findViewById(R.id.uba)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UBA_();
            }
        });

        ((LinearLayout)mDialog.findViewById(R.id.union)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Union_();
            }
        });
        ((LinearLayout)mDialog.findViewById(R.id.polaris)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Polaris_();
            }
        });

        mDialog.show();
    }
    private void GTB_(){
        String[] Method = {"Bank Deposit","GTB App","GT World"};
        Dialog mDialog = new Dialog(activity);
        mDialog.setContentView(R.layout._gtbank);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Spinner spinner = mDialog.findViewById(R.id.spinner2);
        ImageButton imageButton = mDialog.findViewById(R.id.imageButton4);
        ArrayAdapter<String> selfadapter = new ArrayAdapter<>(
                activity, R.layout.my_custom_sp_style, Method);
        selfadapter.setDropDownViewResource(R.layout.my_custom_sp_style);
        ScrollView vBankDeposit = mDialog.findViewById(R.id.bank_deposite);
        spinner.setAdapter(selfadapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               switch (i){
                   case 0:{
                       //Bank transfer
                       vBankDeposit.setVisibility(View.VISIBLE);
                       ( (ScrollView)mDialog.findViewById(R.id.gtAPP)).setVisibility(View.GONE);
                       ( (ScrollView)mDialog.findViewById(R.id.gtWorld)).setVisibility(View.GONE);
                       break;
                   } case 1:{
                       //GT App
                       vBankDeposit.setVisibility(View.GONE);
                       ( (ScrollView)mDialog.findViewById(R.id.gtAPP)).setVisibility(View.VISIBLE);
                       ( (ScrollView)mDialog.findViewById(R.id.gtWorld)).setVisibility(View.GONE);
                       break;
                   } case 2:{
                       //GT World
                       vBankDeposit.setVisibility(View.GONE);
                       ( (ScrollView)mDialog.findViewById(R.id.gtAPP)).setVisibility(View.GONE);
                       ( (ScrollView)mDialog.findViewById(R.id.gtWorld)).setVisibility(View.VISIBLE);
                       break;
                   }
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });



        mDialog.show();
    }

    private void Zenitb_(){
        String[] Method = {"Bank Deposit","Zenith App"};
        Dialog mDialog = new Dialog(activity);
        mDialog.setContentView(R.layout._zenit);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Spinner spinner = mDialog.findViewById(R.id.spinner2);
        ImageButton imageButton = mDialog.findViewById(R.id.imageButton4);
        ArrayAdapter<String> selfadapter = new ArrayAdapter<>(
                activity, R.layout.my_custom_sp_style, Method);
        selfadapter.setDropDownViewResource(R.layout.my_custom_sp_style);
        ScrollView vBankDeposit = mDialog.findViewById(R.id.bank_deposite);
        spinner.setAdapter(selfadapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:{
                        //Bank transfer
                        vBankDeposit.setVisibility(View.VISIBLE);
                        ( (ScrollView)mDialog.findViewById(R.id.gtAPP)).setVisibility(View.GONE);

                        break;
                    } case 1:{
                        //GT App
                        vBankDeposit.setVisibility(View.GONE);
                        ( (ScrollView)mDialog.findViewById(R.id.gtAPP)).setVisibility(View.VISIBLE);

                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });



        mDialog.show();
    }

    private void UBA_(){
        Dialog mDialog = new Dialog(activity);
        mDialog.setContentView(R.layout._uba);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageButton imageButton = mDialog.findViewById(R.id.imageButton4);




        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });



        mDialog.show();
    }
    private void Polaris_(){

        Dialog mDialog = new Dialog(activity);
        mDialog.setContentView(R.layout._polaris);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageButton imageButton = mDialog.findViewById(R.id.imageButton4);



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });



        mDialog.show();
    }
    private void Union_(){

        Dialog mDialog = new Dialog(activity);
        mDialog.setContentView(R.layout._unionbank);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton imageButton = mDialog.findViewById(R.id.imageButton4);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });



        mDialog.show();
    }

}
