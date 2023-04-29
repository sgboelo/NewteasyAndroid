package com.SmartTech.teasyNew;

import com.SmartTech.teasyNew.api_new.appmanager.response_model.PADataValidationResponse;

import java.io.Serializable;
import java.util.List;

public class DataModelnew implements Serializable {
    private int position;
    private List<PADataValidationResponse.Product> Products;

    public DataModelnew() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<PADataValidationResponse.Product> getProducts() {
        return Products;
    }

    public void setProducts(List<PADataValidationResponse.Product> products) {
        Products = products;
    }
}
