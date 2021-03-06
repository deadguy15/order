package com.pdd.order.filter;


import com.pdd.order.model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@Component
@Order(1)
@WebFilter(filterName = "AuthFilter", urlPatterns = "/*" , initParams = {
@WebInitParam(name = "URL", value = "http://localhost:8080")})
public class AuthFilter  implements Filter {

    @Autowired
    Application application;

    private String url;
    /**
     * 可以初始化Filter在web.xml里面配置的初始化参数
     * filter对象只会创建一次，init方法也只会执行一次。
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.url = filterConfig.getInitParameter("URL");
        System.out.println("我是过滤器的初始化方法！URL=" + this.url +  "，生活开始.........");
    }

    /**
     * 主要的业务代码编写方法
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("我是过滤器的执行方法，客户端向Servlet发送的请求被我拦截到了");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        if (!request.getServletPath().equals("/") &&(session.getAttribute("token")==null||session.getAttribute("token").toString().isEmpty()))
        {
            //String site = new String("https://mms.pinduoduo.com/open.html?response_type=code&client_id=6773b28159944ede9702e08f69c5cb46&redirect_uri=http://mimengio.com&state=1212");
            String site = new String("https://mms.pinduoduo.com/open.html?response_type=code&client_id="+application.getClientId()+"&redirect_uri=http://mimengio.com&state=1212");
            response.sendRedirect(site);
            return ;
        }
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("我是过滤器的执行方法，Servlet向客户端发送的响应被我拦截到了");
    }

    /**
     * 在销毁Filter时自动调用。
     */
    @Override
    public void destroy() {
        System.out.println("我是过滤器的被销毁时调用的方法！，活不下去了................" );
    }
}