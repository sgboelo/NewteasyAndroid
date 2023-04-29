package com.SmartTech.teasyNew.api_new.appmanager.response_model;

import com.SmartTech.teasyNew.Unobfuscable;
import com.SmartTech.teasyNew.Utils;

import java.io.Serializable;
import java.util.List;

public class GetInterswitchPaymentItemsResponse extends BaseResponse implements Serializable {

    private List<InterswitchPaymentItem> paymentItems;

    public static class InterswitchPaymentItem implements Serializable, Unobfuscable {
        public String paymentitemname;

        public String paymentCode;

        public long amount;

        /**
         * User sometimes can manually redefine amount
         */
        public long customAmount;

        public Boolean isAmountFixed;

        public InterswitchPaymentItem(String name, String paymentCode, long amount) {
            this.paymentitemname = name;
            this.paymentCode = paymentCode;
            this.amount = amount;
            this.isAmountFixed = true;
        }

        @Override
        public String toString() {
            String result = paymentitemname;
            if (customAmount > 0) {
                result += " - N" + Utils.formatBalance(customAmount);
            } else if (amount > 0 ) {
                result += " - N" + Utils.formatBalance(amount);
            }
            return result;
        }
    }

    public List<InterswitchPaymentItem> getPaymentItems() {
        return paymentItems;
    }

}
