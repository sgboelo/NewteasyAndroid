package com.SmartTech.teasyNew.api_new.appmanager.response_model;

import com.SmartTech.teasyNew.Unobfuscable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionHistoryResponse extends BaseResponse {

    //public Long elementAmount;

    public List<TransactionHistoryEntry> transactions;

    public static class TransactionHistoryEntry implements Unobfuscable {
        public String id;
        public String transactionType;
        public String timestamp;
        public String additionalDestNumber;
        @Deprecated //todo remove
        public String fullAmount;
        public String amount;
        public String fees;
        public String balanceBefore;
        public String balanceAfter;
        public boolean reverted;
        public String initiatorWallet;
        public String destinationWallet;
        public String channel;
        public Map<String, String> additionalData = new HashMap<>();
        public Map<String, String> details = new HashMap<>();

    }

}
