package com.SmartTech.teasyNew.api_new.appmanager.response_model;

import com.SmartTech.teasyNew.Unobfuscable;

import java.util.List;

public class AIRSOutstandingPaymentsResponse extends BaseResponse {

    public List<Outstanding> outstanding;

    public static class Outstanding implements Unobfuscable {
        public String payerName;
        public String payerId;
        public String billRef;
        public long taxId;
        public String taxCode;
        public long amountDue;
        public long amountPaid;
        public long outstanding;
        public int totalPayments;
    }

}
