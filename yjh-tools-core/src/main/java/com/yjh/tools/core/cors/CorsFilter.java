package com.yjh.tools.core.cors;

import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解决跨域拦截器
 *
 * @author yjh
 * */
public class CorsFilter implements Filter {

    protected static String origin;
    protected static String methods;
    protected static String headers;

    @Override
    public void doFilter(ServletRequest res, ServletResponse req, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) req;

        HttpServletRequest reqs = (HttpServletRequest) res;
        response.setHeader("Access-Control-Allow-Origin",origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", methods);
        /*
        resp.addHeader("Access-Control-Max-Age", "0")，表示每次异步请求都发起预检请求，也就是说，发送两次请求。
        resp.addHeader("Access-Control-Max-Age", "1800")，表示隔30分钟才发起预检请求。也就是说，发送两次请求
        */
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", headers);

        if (reqs.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return;
        }
        filterChain.doFilter(res, req);
    }
}
