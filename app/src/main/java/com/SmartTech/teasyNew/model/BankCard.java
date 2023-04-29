package com.SmartTech.teasyNew.model;

import com.SmartTech.teasyNew.Unobfuscable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muddvayne on 26/12/2017.
 */

public class BankCard implements Serializable, Unobfuscable {
    private static List<BIN> binList;
    private String number;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;

    public BankCard(String number, String expiryMonth, String expiryYear, String cvv) {
        this.number = number;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
    }

    public String getNumber() {
        return number;
    }

    public String getNumberFormatted() {
        return formatNumber(number);
    }

    public String getNumberMasked() {
        int length = number.length();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; ++i) {
            if(i < length - 4) {
                //mask all but last 4 numbers
                sb.append("*");
            }
            else {
                sb.append(number.substring(i, i+1));
            }
        }

        return formatNumber(sb.toString());
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public String getCvv() {
        return cvv;
    }

    private String formatNumber(String number) {
        StringBuilder sb = new StringBuilder(number);
        for (int i = sb.length() - 4; i > 0; i -= 4)
            sb.insert(i, ' ');

        return sb.toString();
    }

    public Brand getBrand() {
        return Brand.byCardNumber(number);
    }

    public String getBankName() {
        if(binList == null) {
            try {
                binList = loadBINList();
            }
            catch (IOException e) {
                e.printStackTrace();
                binList = null;
                return "";
            }
        }

        for(BIN bin : binList) {
            if(number.startsWith(bin.number)) {
                return bin.bankName;
            }
        }

        return "";
    }

    private List<BIN> loadBINList() throws IOException {
        List<BIN> list = new ArrayList<>();

        InputStream is = getClass().getClassLoader().getResourceAsStream("bin_list");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            list.add(new BIN(parts[0], parts[1]));
        }

        return list;
    }

    private class BIN {
        private String number;
        private String bankName;

        private BIN(String number, String bankName) {
            this.number = number;
            this.bankName = bankName;
        }
    }

    public enum Brand {
        VISA("^4[0-9]{0,15}$"),
        MASTERCARD("^5[0-9]{0,15}$"),
        AMERICAN_EXPRESS("^3[4,7][0-9]{0,13}$"),
        DISCOVER("^6[0-9]{0,15}$"),
        DINERS_CLUB_AND_CARTE_BLANCHE("^3[0,6,8][0-9]{0,12}$"),
        UNKNOWN("");

        private String pattern;

        Brand(String pattern) {
            this.pattern = pattern;
        }

        public static Brand byCardNumber(String cardNo) {
            for(Brand brand : Brand.values()) {
                if(cardNo.matches(brand.pattern)) {
                    return brand;
                }
            }

            return UNKNOWN;
        }
    }

    /**Used for serialization*/
    public static class BankCardList implements Serializable, Unobfuscable {
        public List<BankCard> list = new ArrayList<>();
    }
}
