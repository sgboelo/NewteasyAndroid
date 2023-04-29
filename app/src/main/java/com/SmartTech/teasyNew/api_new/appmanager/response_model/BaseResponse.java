package com.SmartTech.teasyNew.api_new.appmanager.response_model;

import com.SmartTech.teasyNew.Unobfuscable;

public class BaseResponse implements Unobfuscable {

    public Integer status;

    public String message;

    public ResponseCode getResponseCode() {
        return ResponseCode.byId(status);
    }

    public enum ResponseCode {
        Unknown                         (-1, "Unknown"),
        OK                              (0, "OK"),
        INTERNAL_ERROR                  (1, "Internal Error"),
        INCORRECT_CREDENTIALS           (2, "Incorrect Wallet ID or PIN"),
        CUSTOMER_ALREADY_REGISTERED     (3, "Customer already registered"),
        PARAM_VALIDATION_ERROR          (4, "Param validation error"),
        CUSTOMER_NOT_REGISTERED         (5, "Customer not registered"),
        BANK_ACCOUNT_VALIDATION_FAILED  (6, "Bank account validation failed"),
        METER_NUMBER_INVALID            (7, "Meter number invalid"),
        AMOUNT_TOO_LOW                  (8, "Amount too low"),
        NIS_APPLICATION_NOT_FOUND       (9, "NIS application not found"),
        NIS_APPLICATION_EXPIRED         (10, "NIS application expired"),
        NIS_NOTIFICATION_FAILED         (11, "NIS notification failed"),
        NIS_APP_ALREADY_PAID            (12, "NIS application already paid"),
        BILLER_NOT_FOUND                (13, "Biller not found"),
        REJECTED                        (14, "Rejected by third-party system"),
        FRSC_APPLICATION_NOT_FOUND      (15, "Application not found"),
        FRSC_APP_ALREADY_NOTIFIED       (16, "Application already paid"),
        WALLET_LOCKED_PIN_TRIES_EXCEEDED(17, "Wallet is locked due to PIN tries limit exceeded"),
        WALLET_BLOCKED                  (18, "Wallet is blocked");


        public int code;
        public String message;

        ResponseCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public static ResponseCode byId(int code) {
            for(ResponseCode responseCode : ResponseCode.values()) {
                if(responseCode.code == code) {
                    return responseCode;
                }
            }

            return Unknown;
        }
    }

}
