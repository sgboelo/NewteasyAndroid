package com.SmartTech.teasyNew.api_new.appmanager.response_model;

public class CardChargeAmountValidationResponse extends BaseResponse {

    private long sourceAmount, amountToCharge;
    private String rateDescription;
    private String hash;

    public long getSourceAmount() {
        return sourceAmount;
    }

    public long getAmountToCharge() {
        return amountToCharge;
    }

    public String getRateDescription() {
        return rateDescription;
    }

    public String getHash() {
        return hash;
    }
}
