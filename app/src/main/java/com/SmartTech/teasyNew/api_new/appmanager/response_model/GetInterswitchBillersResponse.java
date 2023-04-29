package com.SmartTech.teasyNew.api_new.appmanager.response_model;

import com.SmartTech.teasyNew.Unobfuscable;

import java.io.Serializable;
import java.util.List;

public class GetInterswitchBillersResponse extends BaseResponse implements Serializable {

    public static class InterswitchBiller implements Serializable, Unobfuscable {
        public String billerId;

        public String name;

        public InterswitchBiller() {
            //for serialization
        }

        public InterswitchBiller(String billerId, String name) {
            this.billerId = billerId;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private List<InterswitchBiller> billers;

    public List<InterswitchBiller> getBillers() {
        return billers;
    }
}
