package com.SmartTech.teasyNew;

public class netWorkCheck {
    public netWorkCheck() {
    }

    public String networkByPhone(String phone) {
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
            case "234703":
            case "234806":
            case "234803":
            case "234706":
            case "234810":
            case "234813":
            case "234814":
            case "234816":
            case "234903":
            case "234913":
                return "MTN";

            case "234705":
            case "234805":
            case "234807":
            case "234811":
            case "234815":
            case "234905":
            case "234915":
                return "GLO";

            case "234701":
            case "234708":
            case "234802":
            case "234812":
            case "234902":
            case "234808":
            case "234907":
                return "AIRTEL";

            case "234809":
            case "234817":
            case "234818":
            case "234909":
                return "9MOBILE";

            //case "234819": return STARCOMMS;

            //case "234704": return VISAFONE;
        }

        return "UNKNOWN";
    }
}
