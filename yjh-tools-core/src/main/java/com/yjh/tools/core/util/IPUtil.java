package com.yjh.tools.core.util;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {

    /**
     * 获取 IP 地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        ip = ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;

        if (ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * IP 转 long
     * @param ip
     * @return
     */
    public static long ipToNumber(String ip) {
        String[] segment = ip.split("\\.");
        if (segment.length != 4) {
            return 0;
        }

        return (Long.parseLong(segment[0]) << 24) +
                (Long.parseLong(segment[1]) << 16) +
                (Long.parseLong(segment[2]) << 8) +
                (Long.parseLong(segment[3]));
    }

    /**
     * long 转 IP
     * @param number
     * @return
     */
    public static String numberToIp(long number) {
        return String.valueOf(number >>> 24) + '.' +
                ((number & 0x00ffffff) >>> 16) + '.' +
                ((number & 0x0000ffff) >>> 8) + '.' +
                ((number & 0x000000ff));
    }
}
