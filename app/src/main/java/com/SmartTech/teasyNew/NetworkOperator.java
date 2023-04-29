package com.SmartTech.teasyNew;

/**
 * Created by muddvayne on 06/07/2017.
 */

/**
* This enum is being serialized inside AirtimeRechargeInfo. Do not forget to update proguard's rules if you move it
* */
public enum NetworkOperator {
    UNKNOWN("-1"),

    MTN("001"),
    AIRTEL("002"),
    ETISALAT("003"),
    GLO("004"),
    VISAFONE("005"),
    STARCOMMS("006");

    public final String code;

    NetworkOperator(String code) {
        this.code = code;
    }

    public static NetworkOperator networkByPhone(String phone) {
        String international = Utils.phoneToInternational(phone.replaceAll("[^0-9]",""));

        /** uncomment when ready to add support for STARCOMMS and VISAFONE */

        /*String prefix = international.substring(0, Math.min(7, international.length()));

        switch (prefix) {
            case "2347028": return STARCOMMS;
            case "2347029": return STARCOMMS;

            case "2347025": return VISAFONE;
            case "2347026": return VISAFONE;
        }*/

        String prefix = international.substring(0, Math.min(6, international.length()));
        switch (prefix) {
            case "234703": return MTN;
            case "234706": return MTN;
            case "234803": return MTN;
            case "234806": return MTN;
            case "234810": return MTN;
            case "234813": return MTN;
            case "234814": return MTN;
            case "234816": return MTN;
            case "234903": return MTN;

            case "234705": return GLO;
            case "234805": return GLO;
            case "234807": return GLO;
            case "234811": return GLO;
            case "234815": return GLO;
            case "234905": return GLO;

            case "234701": return AIRTEL;
            case "234708": return AIRTEL;
            case "234802": return AIRTEL;
            case "234808": return AIRTEL;
            case "234812": return AIRTEL;
            case "234902": return AIRTEL;
            case "234907": return AIRTEL;

            case "234809": return ETISALAT;
            case "234817": return ETISALAT;
            case "234818": return ETISALAT;
            case "234909": return ETISALAT;

            //case "234819": return STARCOMMS;

            //case "234704": return VISAFONE;
        }

        return UNKNOWN;
    }
}
