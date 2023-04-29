package com.SmartTech.teasyNew.api_new.appmanager.response_model;

import com.SmartTech.teasyNew.Unobfuscable;
import com.SmartTech.teasyNew.Utils;

import java.io.Serializable;
import java.util.List;

public class PADataValidationResponse extends BaseResponse {

    public Opts opts;
    public List<Product> products;

    public static class Opts {
        public boolean hasOpenRange;
        public String country;
        public String operator;
        public String iso;
        public String msisdn;
    }

    public static class Product implements Unobfuscable, Serializable {
        public String product_id;
        public boolean openRange;
        public String topup_currency;
        public String currency;
        public String validity;
        public String face_value;
        public String topup_value;
        public String price;
        public String denomination;
        public String data_amount;

        public String displayName() {
            long faceValue = Long.parseLong(face_value) * 100;
            String price = Utils.formatBalance(faceValue).split("\\.")[0];
            String dataAmountStr = null;

            float dataAmount = Float.parseFloat(data_amount);
            if(dataAmount < 1000) {
                dataAmountStr = dataAmount + "MB";
            }
            else {
                dataAmountStr = (dataAmount / 1000) + "GB";
            }

            return String.format("%s - %s - %s", "N"+price, dataAmountStr, validity);
        }
    }

}
