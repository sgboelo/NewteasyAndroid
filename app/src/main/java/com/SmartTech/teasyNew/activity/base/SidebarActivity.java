package com.SmartTech.teasyNew.activity.base;

/**
 * Created by muddvayne on 21/06/2017.
 */

public class SidebarActivity extends BaseActivity {

    
    private String getDisplayName() {
        switch (session.getAccountType()) {
            case CUSTOMER:
                String customerFirstName = session.getCustomerFirstName();
                String customerMiddleName = session.getCustomerMiddleName();
                String customerLastName = session.getCustomerLastName();

                String fullName = customerFirstName;
                if(customerMiddleName != null && !"null".equals(customerMiddleName)) {
                    fullName += " " + customerMiddleName;
                }
                fullName += " " + customerLastName;
                return fullName;

            case AGENT:
                return session.getAgentName().replaceAll("[0-9]{6} - ", "");
        }

        return "";
    }

}
