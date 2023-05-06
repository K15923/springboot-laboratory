package com.k.service.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k.service.api.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author: chenqingyu
 * @date: 2022/8/19 14:33
 **/
@Slf4j
@Aspect
@Profile({"office", "dev", "pre", "test"})
@Component
public class FeignAspect {
    @Autowired
    @Lazy
    private ObjectMapper objectMapper;

    /**
     * 设置切入点, 切所有模块的Feign接口
     */

    @Pointcut("execution(* com.k.service.api..manager..*.*(..)) ")
    public void handle() {
    }

    @AfterReturning(returning = "result", pointcut = "handle()")
    public void doAfterReturning(JoinPoint joinPoint, Result result) {
        String methodName = joinPoint.getSignature().getName();
        String typeName = joinPoint.getSignature().getDeclaringType().getName();
        tryDebug(joinPoint);
        if (Objects.isNull(result) || !Integer.valueOf(200).equals(result.getCode())) {
            String msg = String.format("feign调用异常-result: %s", result.toString());
            log.error("返回信息: {}, 代理类名: {}, 方法名: {}.", msg, typeName, methodName);
        }
    }

    /**
     * 打印Feign接口入参数据
     *
     * @param joinPoint
     */
    private void tryDebug(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String typeName = joinPoint.getSignature().getDeclaringType().getName();
        int len;
        if (args != null && (len = args.length) > 0) {
            StringBuilder strParams = new StringBuilder();
            try {
                for (int i = 0; i < len; ++i) {
                    strParams.append(objectMapper.writeValueAsString(args[i]));
                    if (i != len - 1) {
                        strParams.append('\n');
                    }
                }
                log.info("feign接口 {}#{}的请求入参: {}", typeName, methodName, strParams.toString());
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
    }

    @AfterThrowing(throwing = "th", pointcut = "handle()")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable th) {
        String methodName = joinPoint.getSignature().getName();
        String typeName = joinPoint.getSignature().getDeclaringType().getName();
        tryDebug(joinPoint);
        String msg = String.format("feign调用异常-result: %s", th.getMessage());
        log.error("返回信息: {}, 代理类名: {}, 方法名: {}.", msg, typeName, methodName);
    }
}
