package com.SmartTech.teasyNew.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DataAirtimeModel implements Serializable {
    private String Type;
    private String network;
    private Map<String, String> data = new HashMap<>();

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
