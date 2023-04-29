package com.SmartTech.teasyNew.api_new.appmanager.response_model;

import com.SmartTech.teasyNew.Unobfuscable;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class AIRSTaxListResponse extends BaseResponse {

    public List<Collection> collections;

    public static class Tax implements Unobfuscable, Serializable {
        public Long id;

        public String code;

        public Long price;

        public Set<String> tags;
    }

    public static class Collection implements Unobfuscable {
        public String code;
        public List<Tax> taxes;
    }

}
