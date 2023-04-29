package com.SmartTech.teasyNew.api_new.appmanager.response_model;

public class KIRSPaymentResponse extends BaseResponse {
    public String transactionRefNumber, kirsResponse;
    public String paymentType;

    public Long totalAmountDue, amountPaid, outstanding;
}
