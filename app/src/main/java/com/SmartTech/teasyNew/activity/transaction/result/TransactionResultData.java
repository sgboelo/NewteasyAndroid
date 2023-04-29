package com.SmartTech.teasyNew.activity.transaction.result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.SmartTech.teasyNew.activity.base.BaseActivity;
import com.SmartTech.teasyNew.utils.SerializableRunnable;
import com.SmartTech.teasyNew.utils.receipt.Receipt;

import java.io.Serializable;

/**
 * Created by muddvayne on 09/09/2017.
 */

/**
 * This object is intended to be sent to ActivityTransactionResult via intent
 * to generate widgets and callbacks
 * */
public class TransactionResultData implements Serializable {

    private TransactionResult result;

    private SerializableRunnable buttonOnClickListener;

    private Class<? extends BaseActivity> previousActivityClass;

    private String errorHeader, errorDescription, activityTitle, buttonText;

    private Receipt receipt;

    private boolean buttonVisible = true;

    public TransactionResultData() {

    }

    public TransactionResultData(
            @NonNull Class<? extends BaseActivity> previousActivityClass,
            @NonNull TransactionResult result) {

        this.previousActivityClass = previousActivityClass;
        this.result = result;
    }

    public TransactionResult getResult() {
        return result;
    }

    public void setResult(TransactionResult result) {
        this.result = result;
    }

    /**
     * This activity will be called when user presses device's or dashboard's back button
     * */
    public Class<? extends BaseActivity> getPreviousActivityClass() {
        return previousActivityClass;
    }

    /**
     * This activity will be called when user presses device's or dashboard's back button
     * */
    public void setPreviousActivityClass(Class<? extends BaseActivity> previousActivityClass) {
        this.previousActivityClass = previousActivityClass;
    }

    @Nullable
    public SerializableRunnable getButtonOnClickListener() {
        return buttonOnClickListener;
    }

    @Nullable
    public String getErrorDescription() {
        return errorDescription;    //todo: rename
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public void setButtonOnClickListener(SerializableRunnable buttonOnClickListener) {
        this.buttonOnClickListener = buttonOnClickListener;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getErrorHeader() {
        return errorHeader;
    }

    public void setErrorHeader(String errorHeader) {
        this.errorHeader = errorHeader;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public boolean isButtonVisible() {
        return buttonVisible;
    }

    public void setButtonVisible(boolean buttonVisible) {
        this.buttonVisible = buttonVisible;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public enum TransactionResult {
        SUCCESS, FAIL
    }

}
