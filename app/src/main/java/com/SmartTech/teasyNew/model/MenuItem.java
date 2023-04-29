package com.SmartTech.teasyNew.model;

import com.google.gson.annotations.SerializedName;
import com.SmartTech.teasyNew.Unobfuscable;

import java.io.Serializable;

/**
 * Created by muddvayne on 26/09/2017.
 */

public class MenuItem implements Unobfuscable, Serializable {

    @SerializedName("stringId")
    private String id;

    private boolean enabledForCustomers;

    private boolean enabledForAgents;

    public MenuItem(String id, boolean enabledForCustomers, boolean enabledForAgents) {
        this.id = id;
        this.enabledForCustomers = enabledForCustomers;
        this.enabledForAgents = enabledForAgents;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabledForCustomers() {
        return enabledForCustomers;
    }

    public boolean isEnabledForAgents() {
        return enabledForAgents;
    }
}
