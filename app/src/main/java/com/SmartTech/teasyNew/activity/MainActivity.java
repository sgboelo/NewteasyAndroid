package com.SmartTech.teasyNew.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.SmartTech.teasyNew.FontsOverride;
import com.SmartTech.teasyNew.Home;
import com.SmartTech.teasyNew.ManageCard;
import com.SmartTech.teasyNew.PayBills;
import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.Session;
import com.SmartTech.teasyNew.TranferCash;
import com.SmartTech.teasyNew.activity.annotation.Lockable;
import com.SmartTech.teasyNew.activity.base.BaseActivity;
import com.SmartTech.teasyNew.model.ContactModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

@Lockable
public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener, Home.SessionTransfer {
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(session == null) {
            Intent intent = new Intent(this, ActivitySplash.class);
            startActivity(intent);
            finish();
            return;
        }

        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Roboto-Medium.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Roboto-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Roboto-Light.ttf");
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.Home);


    }

    @Override
    public void onBackPressed() {


        if (getFragmentManager().getBackStackEntryCount() == 0) {
            if(bottomNavigationView.getSelectedItemId() != R.id.Home) {
                fragment = new Home();
                getSupportFragmentManager().beginTransaction()
                        .commit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
                bottomNavigationView.setSelectedItemId(R.id.Home);
            }else{
                ExitApp exitApp = new ExitApp(this);
                exitApp.Exit("Do You Want To Logout ?");
                exitApp.getYes().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
                        ArrayList<ContactModel> mContacts = getSession().getContacts();
                        intent.putExtra("Smart_Contacts", mContacts);
                        startActivity(intent);
                        finish();
                    }
                });
                exitApp.getNo().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exitApp.getPinDialog().dismiss();
                    }
                });
                exitApp.getPinDialog().show();

            }
        } else {
            getFragmentManager().popBackStack();
        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Home: {

                fragment = new Home();
                getSupportFragmentManager().beginTransaction()
                        .commit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
                return true;
            }
            case R.id.Transfer: {

                fragment = new TranferCash();
                getSupportFragmentManager().beginTransaction()
                        .commit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
                return true;
            }
            case R.id.Card:{
                fragment = new ManageCard();
                getSupportFragmentManager().beginTransaction()
                        .commit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
                return true;
            }
            case R.id.PayBill:{
                fragment = new PayBills();
                getSupportFragmentManager().beginTransaction()
                        .commit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,
                                fragment).commit();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void sessionTransferListener(Session s) {
        session = s;
    }
}
