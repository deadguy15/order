package com.pdd.order.controller;
import com.alibaba.fastjson.*;
import com.pdd.order.model.Application;
import com.pdd.order.model.Shop;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddOrderListGetRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddOrderListGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("access_token")
public class OrderController
{

    @Autowired
    Application application;


    @RequestMapping("/login2")
    public String login(Model model)
    {
        return "login";
    }

    @RequestMapping(value = "/")
    public String index(String code , String state, Model model, HttpServletRequest servletRequest){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://open-api.pinduoduo.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject content = new JSONObject();//放入body中的json参数
        String clientId = application.getClientId();
        String clientSecret = application.getClientSecret();
        content.put("client_id", clientId);
        content.put("code",code);
        content.put("grant_type","authorization_code");
        content.put("client_secret", clientSecret);


        HttpEntity<JSONObject> request = new HttpEntity<>(content,headers); //组装
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        String body = response.getBody();
        JSONObject res = JSONObject.parseObject(body);
        String refresh_token = res.getString("access_token");
        Shop s =new Shop();
        s.setAccessToken(refresh_token);
        if (refresh_token!=null&&!refresh_token.isEmpty()) {
            model.addAttribute("token", refresh_token);
            servletRequest.getSession().setAttribute("token", refresh_token);
        }
        else
        {
            return "login";
        }
        System.out.println(body);
        System.out.println("token: "+refresh_token);
        return "index";
    }
    @RequestMapping(value = "/list")
    public void list (Model model) throws Exception
    {

        String clientId =application.getClientId();
        String clientSecret =application.getClientSecret();
        String accessToken =(String)model.getAttribute("token");
        System.out.println("clientId: "+clientId);
        System.out.println("clientSecret: "+clientSecret);
        System.out.println("accessToken: "+accessToken);
        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddOrderListGetRequest request = new PddOrderListGetRequest();
        request.setOrderStatus(1);
        request.setRefundStatus(1);
        request.setStartConfirmAt(1L);
        request.setEndConfirmAt(1L);
        request.setPage(1);
        request.setPageSize(1);
        request.setTradeType(1);
        request.setUseHasNext(false);
        PddOrderListGetResponse response = client.syncInvoke(request, accessToken);
        System.out.println(JsonUtil.transferToJson(response));
    }

    @RequestMapping(value = "/refersh")
    public void refresh_token (Model model) throws Exception {

        String clientId =(String)model.getAttribute("client_id");
        String clientSecret =(String)model.getAttribute("client_secret");
        String accessToken =(String)model.getAttribute("token");

        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddOrderListGetRequest request = new PddOrderListGetRequest();
        request.setOrderStatus(1);
        request.setRefundStatus(1);
        request.setStartConfirmAt(1L);
        request.setEndConfirmAt(1L);
        request.setPage(1);
        request.setPageSize(1);
        request.setTradeType(1);
        request.setUseHasNext(false);
        PddOrderListGetResponse response = client.syncInvoke(request, accessToken);
        System.out.println(JsonUtil.transferToJson(response));
    }
}
