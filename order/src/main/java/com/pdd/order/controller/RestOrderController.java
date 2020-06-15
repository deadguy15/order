package com.pdd.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestOrderController
{
    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }

}
