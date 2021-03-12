package com.yjh.tools.core.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    /**
     * 获取 ip
     * @return
     */
    public static String getIp() {
        return IPUtil.getIpAddr(getHttpServletRequest());
    }

    public static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        return request;
    }

    /**
     * 获取请求头参数值
     * @param headerParam
     * @return
     */
    public static String getHeader(String headerParam) {
        return getHttpServletRequest().getHeader(headerParam);
    }
}
