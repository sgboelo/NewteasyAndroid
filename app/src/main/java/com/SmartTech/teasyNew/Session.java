package com.SmartTech.teasyNew;

import static com.SmartTech.teasyNew.Session.AccountType.AGENT;

import com.SmartTech.teasyNew.model.ContactModel;
import com.SmartTech.teasyNew.model.DeviceInfo;
import com.SmartTech.teasyNew.model.MenuItem;
import com.SmartTech.teasyNew.model.SavedBeneficiary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by muddvayne on 05/04/2017.
 */

public class Session implements Serializable{

    public String walletId, pin;
    public ArrayList<ContactModel> contacts;
    private String mainBalance;

    private String commissionBalance;

    public String getMainBalance() {
        return mainBalance;
    }

    public void setMainBalance(String mainBalance) {
        this.mainBalance = mainBalance;
    }

    public String getCommissionBalance() {
        return commissionBalance;
    }

    public void setCommissionBalance(String commissionBalance) {
        this.commissionBalance = commissionBalance;
    }

    private String customerFirstName, customerMiddleName, customerLastName;
    private String agentName;

    private String agentShortCode;

    private List<MenuItem> menuItems;

    private AccountType accountType;

    private DeviceInfo deviceInfo;

    public List<String> iopAvailableCuntries;

    public String aesKey;
    public int kycLevel;
    public List<SavedBeneficiary> savedBeneficiaries;

    /**
     * Cache for saving some information.
     * Is wiped on logout
     */
    private Map<String, Serializable> cache = new HashMap<>();

    public void setCustomerFirstName(String value) {
        customerFirstName = value;
    }

    public void setCustomerMiddleName(String value) {
        customerMiddleName = value;
    }

    public void setCustomerLastName(String value) {
        customerLastName = value;
    }

    public void setMenuItems(List<MenuItem> items) {
        menuItems = items;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerMiddleName() {
        return customerMiddleName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAgentShortCode() {
        return accountType == AGENT ? agentShortCode : "";
    }

    public void setAgentShortCode(String agentShortCode) {
        this.agentShortCode = agentShortCode;
    }

    public Map<String, Serializable> getCache() {
        return cache;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public ArrayList<ContactModel> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<ContactModel> contacts) {
        this.contacts = contacts;
    }

    public enum AccountType {
        CUSTOMER, AGENT, AGGREGATOR, CORPORATE
    }
}
