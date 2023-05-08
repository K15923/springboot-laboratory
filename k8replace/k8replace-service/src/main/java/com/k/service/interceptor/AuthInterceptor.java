package com.k.service.interceptor;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.k.service.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthInterceptor extends HandlerInterceptorAdapter {
    private final static Map<String, Integer> IGNORE_PATH = new HashMap<>();

    static {
        IGNORE_PATH.put("", 0);
        IGNORE_PATH.put("", 0);

    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("authorization");
        String phone = "";
        if (StringUtils.isBlank(token) ||
            ("null".equals(token) && StringUtils.isNotBlank(phone) && !"null".equals(phone))) {
            return true;
        }

        String requestURI = request.getRequestURI();
        boolean isJump = IGNORE_PATH.containsKey(requestURI);

        throw new RuntimeException();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
