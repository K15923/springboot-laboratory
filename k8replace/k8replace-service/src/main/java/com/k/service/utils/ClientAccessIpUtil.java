package com.k.service.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XY
 * @despribe 访问ip获取工具类
 * @createTime 2018年10月6日 下午2:38:34
 */
public class ClientAccessIpUtil {

    private static final String[] HEADERS_TO_TRY = {"Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR", "X-Real-IP"};

    /**
     * @despriton:获取客户端ip地址(可以穿透代理)
     * @parem: 2018年10月6日 String
     */
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        // 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，取X-Forwarded-For中第一个非unknown的有效IP字符串
        if (null != ip && !"".equals(ip.trim()) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }

        // 以其他方式获取ip
        for (String header : HEADERS_TO_TRY) {
            ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
