package com.k.service.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Description:参数效验拦截器 Author Version Date Changes zhuangjianfa 1.0 2018年2月1日 Created
 */
@Order(1000)
@Aspect
@Component
@Slf4j
public class ValidateParameterAspectAdvice {

    @Before("execution(*  com.k.service.api.*.*(..))")
    public void before(JoinPoint pjd) {
        Object[] args = pjd.getArgs();
        if (args.length > 0) {
            Object oneParam = args[0];
            if (true) {
                log.error("ParamsValidateException , class={}, method={}, msg={}",
                          pjd.getTarget().getClass().getSimpleName(), pjd.getSignature().getName());
                throw new RuntimeException();
            }
        }
    }
}
