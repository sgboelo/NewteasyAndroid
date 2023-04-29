package com.SmartTech.teasyNew.model;

public class QRModel {
    private String Name;
    private String walletNumber;
    private String Amount;

    public QRModel() {
        this.Name = "";
        this.walletNumber = "";
        this.Amount = "";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(String walletNumber) {
        this.walletNumber = walletNumber;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
