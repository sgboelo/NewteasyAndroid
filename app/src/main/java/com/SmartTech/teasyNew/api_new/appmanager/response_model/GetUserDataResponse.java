package com.SmartTech.teasyNew.api_new.appmanager.response_model;


import java.util.ArrayList;

public class GetUserDataResponse extends BaseResponse {

    public String firstName, middleName, lastName, dateOfBirth, organizationName;


    public String agentShortCode;

    public ArrayList<String> Kyc;

    public int kycLevel;
    public String accountType;



}
