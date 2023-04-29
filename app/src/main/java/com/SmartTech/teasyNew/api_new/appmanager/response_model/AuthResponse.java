package com.SmartTech.teasyNew.api_new.appmanager.response_model;

import com.SmartTech.teasyNew.model.MenuItem;
import com.SmartTech.teasyNew.model.SavedBeneficiary;

import java.util.ArrayList;
import java.util.List;

public class AuthResponse extends BaseResponse {

    public String firstname, middlename, lastname;
    public String agentName;
    public String mainBalance, commissionBalance;
    public String agentShortCode;
    public String accountType;
    public String aesKey;
    public int kycLevel;
    public List<MenuItem> menuItemList;
    public List<String> iopAvailableCuntries = new ArrayList<>();
    public List<SavedBeneficiary> savedBeneficiaries;

}
