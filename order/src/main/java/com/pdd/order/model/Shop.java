package com.pdd.order.model;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

public class Shop {

    String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
