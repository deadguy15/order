package com.pdd.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class OrderApplication {

    @Value("${clientId}")
    private static String clientId;

    @Value("${clientSecret}")
    private static String clientSecret;


    public static void main(String[] args) {
        Map<String, String> getenv = System.getenv();
        System.out.println("clientId:"+clientId);
        System.out.println("clientSecret:"+clientSecret);
        SpringApplication.run(OrderApplication.class, args);

    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

}
