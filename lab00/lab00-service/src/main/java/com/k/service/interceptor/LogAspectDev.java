package com.k.service.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@Component
@Profile({"office", "dev", "pre", "test"})
@Aspect
public class LogAspectDev {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspectDev.class);


    @Pointcut("execution(* com.k.service..controller..*.*(..))")
    public void printLog() {

    }

    @Before("execution(* com.k.service..controller..*.*(..))")
    public void printLogBefore(JoinPoint joinPoint) {
        Optional.ofNullable(joinPoint).ifPresent(var -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            LOGGER.info("请求URL：{}, 请求方式：{}，调用路径：{}，请求参数：{}，异常信息：{}",
                    request.getRequestURL().toString(), request.getMethod(),
                    String.join(".", joinPoint.getSignature().getDeclaringTypeName(),
                            joinPoint.getSignature().getName()), Arrays.toString(joinPoint.getArgs()));
        });
    }
}
