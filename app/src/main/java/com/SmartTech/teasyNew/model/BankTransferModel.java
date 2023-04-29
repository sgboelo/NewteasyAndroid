package com.SmartTech.teasyNew.model;

import com.SmartTech.teasyNew.Session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BankTransferModel implements Serializable {
    private String currentFragment;
    private Session session;
    private String  accNumber;
    private String bankCode;
    private String accountName;
    private String previousFragment;
    private String bankName;
    private String narration;
    Map<String, String> BankTransferData = new HashMap<>();
    private String AccountType;

    public BankTransferModel() {
        this.currentFragment = "";
        this.accNumber = "";
        this.bankCode = "";
        this.accountName = "";
        this.previousFragment = "";
        this.bankName = "";
        this.narration = "";
        this.AccountType = "";
    }


    public String getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(String currentFragment) {
        this.currentFragment = currentFragment;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPreviousFragment() {
        return previousFragment;
    }

    public void setPreviousFragment(String previousFragment) {
        this.previousFragment = previousFragment;
    }

    public String getBankName() {
        return bankName;
    }

    public Map<String, String> getBankTransferData() {
        return BankTransferData;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public void setBankTransferData(Map<String, String> bankTransferData) {
        BankTransferData = bankTransferData;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }
}
