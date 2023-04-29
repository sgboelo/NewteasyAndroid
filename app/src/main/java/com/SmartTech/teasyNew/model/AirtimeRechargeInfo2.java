package com.SmartTech.teasyNew.model;

import com.SmartTech.teasyNew.NetworkOperator;

import java.io.Serializable;

/**
 * Created by muddvayne on 17/08/2017.
 */

public class AirtimeRechargeInfo2 implements Serializable {

    public String receiverPhone, receiverName;

    public long amount;

    public NetworkOperator network;

    public AirtimeRechargeInfo2(String receiverPhone, long amount, NetworkOperator network, String receiverName) {
        this.receiverPhone = receiverPhone;
        this.amount = amount;
        this.network = network;
        this.receiverName = receiverName;
    }

    public AirtimeRechargeInfo2(String receiverPhone, long amount, NetworkOperator network) {
        this(receiverPhone, amount, network, null);
    }
}
