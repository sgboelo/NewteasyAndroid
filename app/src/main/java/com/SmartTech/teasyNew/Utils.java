package com.SmartTech.teasyNew;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by muddvayne on 05/04/2017.
 */

public class Utils {

    public static String formatBalance(String balance) {
        String sign = "";

        if(balance.startsWith("-")) {
            balance = balance.replace("-", "");
            sign = "-";
        }

        if(balance.length() == 1) {
            return sign + "0.0" + balance;
        }

        if(balance.length() == 2)  {
            return sign + "0." + balance;
        }

        int length = balance.length();
        long integerPart = Long.valueOf(sign + balance.substring(0, length-2));
        String decimalPart = balance.substring(length-2, length);

        return NumberFormat.getNumberInstance(Locale.US).format(integerPart) + "." + decimalPart;
    }

    public static String formatBalance(long balance) {
        String value = String.valueOf(balance);
        return formatBalance(value);
    }

    public static String phoneToInternational(String phone) {
        String phoneClean = phone.replaceAll("[^0-9]", "");

        if(phoneClean.startsWith("0")) {
            return "234" + phone.substring(1);
        }

        return phoneClean;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(imm == null) {
            return;
        }

        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void vibrate(Context context, long duration) {
        try {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(duration);
            }
        }
        catch (Exception e) {
            //do nothing
        }
    }
}
