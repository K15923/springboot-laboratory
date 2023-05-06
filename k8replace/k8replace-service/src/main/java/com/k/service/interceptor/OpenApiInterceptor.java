package com.k.service.interceptor;

import com.k.service.annotation.SignatureVerification;
import com.k.service.util.ThreadLocalUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

@Component
@Slf4j
public class OpenApiInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        SignatureVerification anno = getAnno(method);
        return true;
    }

    private SignatureVerification getAnno(HandlerMethod handlerMethod) {
        return handlerMethod.getMethodAnnotation(SignatureVerification.class);
    }

    private void setLocalData(HandlerMethod method, Map pathVariables, TreeMap<Object, Object> bodyParams,
                              SignatureVerification anno) {

    }

    @SneakyThrows
    private String getUserPhone(HandlerMethod handlerMethod, Map pathParam, Map bodyParam) {
        throw new RuntimeException();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
