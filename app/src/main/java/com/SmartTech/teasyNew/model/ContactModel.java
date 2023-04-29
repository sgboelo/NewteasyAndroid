package com.SmartTech.teasyNew.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactModel implements Serializable {
    private String DisplayName;
    private String ContractID;

    public ContactModel() {
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }
    public String getContractID() {
        return ContractID;
    }

    public void setContractID(String contractID) {
        ContractID = contractID;
    }
}
