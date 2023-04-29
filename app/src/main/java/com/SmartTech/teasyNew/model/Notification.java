package com.SmartTech.teasyNew.model;

import com.SmartTech.teasyNew.Unobfuscable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by muddvayne on 12/10/2017.
 */

public class Notification implements Serializable, Unobfuscable {

    public int type;

    public int status;

    public int direction;

    public long amount;

    public String remarks;

    public Date created;

    public Notification(NotificationType type, NotificationStatus status, Direction direction,
                        long amount, String remarks, Date created) {

        this.type = type.id;
        this.status = status.id;
        this.direction = direction.id;
        this.amount = amount;
        this.remarks = remarks;
        this.created =  created;
    }

    public NotificationStatus getStatus() {
        return NotificationStatus.byID(this.status);
    }

    public Direction getDirection() {
        return Direction.byID(this.direction);
    }

    public NotificationType getType() {
        return NotificationType.byID(this.type);
    }

    public enum NotificationType implements Unobfuscable{
        UNKNOWN(-1, "Transaction"),
        AIRTIME_PURCHASE(1, "Airtime & Data"),
        BANK_TRANSFER(2, "Bank Transfer"),
        FUND_WALLET_VOUCHER(3, "Wallet Funding (Voucher)"),
        WALLET_TRANSFER(4, "Wallet Transfer"),
        PAY_BILL_AEDC(5, "Pay Bill (AEDC)"),
        MAKE_PAYMENT_HOSPITAL(6, "Make Payment (Hospital)"),
        PAY_BILL(7, "Bill Payment"),
        MAKE_PAYMENT_NIS(8, "Make Payment (NIS)"),
        PAY_BILL_INTERSWITCH(9, "Transaction"),
        AIRS(10, "AIRS");

        private int id;
        private String displayName;

        NotificationType(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public static NotificationType byID(int id) {
            for(NotificationType value : values()) {
                if(value.id == id) {
                    return value;
                }
            }

            return UNKNOWN;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum NotificationStatus implements Unobfuscable{
        SUCCESS(1),
        FAIL(2);

        private int id;

        NotificationStatus(int id) {
            this.id = id;
        }

        public static NotificationStatus byID(int id) {
            for(NotificationStatus value : values()) {
                if(value.id == id) {
                    return value;
                }
            }

            return null;
        }
    }

    public enum Direction implements Unobfuscable{
        INCOMING(1),
        OUTGOING(2);

        private int id;

        Direction(int id) {
            this.id = id;
        }

        public static Direction byID(int id) {
            for(Direction value : values()) {
                if(value.id == id) {
                    return value;
                }
            }

            return null;
        }
    }

}
