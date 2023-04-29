package com.SmartTech.teasyNew.api_new.appmanager.response_model;

public class AIRSBillStatusResponse extends BaseResponse {

    public String payerFirstname, payerLastname;
    public Long amountDue, amountPaid, outstanding;
    public String taxCode;
    public Long taxId;

}
