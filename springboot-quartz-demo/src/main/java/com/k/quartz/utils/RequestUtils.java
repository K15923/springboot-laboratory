package com.k.quartz.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Request工具类
 */
public class RequestUtils {

    /**
     * 获取Request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getRequest();
    }

    /**
     * 获取URI
     *
     * @return
     */
    public static String getUri() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getRequestURI();
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public static String getIpAddress() {
        return getIpAddress(getRequest());
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String unknown = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取Cookie
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 设置Cookie
     *
     * @param response
     * @param cookieName
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);
        response.addCookie(cookie);
    }

    /**
     * 设置Cookie
     *
     * @param response
     * @param cookieName
     * @param value
     * @param cookieMaxAge
     */
    public static void setCookie(HttpServletResponse response, String cookieName, String value, int cookieMaxAge) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(cookieMaxAge);
        response.addCookie(cookie);
    }

    /**
     * 删除Cookie
     *
     * @param response
     * @param cookieName
     */
    public static void deleteCookie(HttpServletResponse response, String cookieName) {
        setCookie(response, cookieName, null, 0);
    }
}
