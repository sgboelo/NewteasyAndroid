package com.SmartTech.teasyNew.model;

import java.util.List;

public class historyBankTransferModel {

   private String targetAccountName;
   private String destinationCode;
   private String bvn;
   private String sourceAccountName;

    public historyBankTransferModel() {
        targetAccountName = "targetAccountName";
        destinationCode = "destinationCode";
        bvn = "bvn";
        sourceAccountName = "sourceAccountName";
    }


    public String getTargetAccountName() {
        return targetAccountName;
    }

    public void setTargetAccountName(String targetAccountName) {
        this.targetAccountName = targetAccountName;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getSourceAccountName() {
        return sourceAccountName;
    }

    public void setSourceAccountName(String sourceAccountName) {
        this.sourceAccountName = sourceAccountName;
    }


    public String getBankNameH(){
        List<Bank> mBank = Bank.byType(Bank.Type.BANK);
        mBank.addAll(Bank.byType(Bank.Type.MMO));
        String BankName = "";
        for (int i = 0; i < mBank.size(); ++i){
            if(mBank.get(i).getCode().equals(destinationCode)){
                BankName = mBank.get(i).getDisplayName();
                break;
            }
        }
        return  BankName;
    }
}
