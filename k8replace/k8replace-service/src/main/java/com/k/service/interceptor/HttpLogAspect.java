package com.k.service.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k.service.utils.ClientAccessIpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @author XY
 * @despribe http请求log切面
 * @createTime 2018年5月28日 上午8:33:00
 */
@Slf4j
// @Aspect
// @Order(-10000) // 保证在其他切面前执行
// @Component
public class HttpLogAspect {

    private static ThreadLocal<Long> startTime = new ThreadLocal<>();
    private static ThreadLocal<String> key = new ThreadLocal<>();
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 定义拦截规则:拦截http请求
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) " +
              "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
              "|| @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void pointcutDefine() {
    }

    /**
     * 请求方法前打印内容
     *
     * @param joinPoint
     */
    @Before("pointcutDefine()")
    public void doBefore(JoinPoint joinPoint) {

        // 请求开始时间
        startTime.set(System.currentTimeMillis());
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra == null) {
            log.warn("【http请求切面，获取RequestAttributes失败】");
            return;
        }

        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        // // 如果有session则返回session 如果没有则返回null(避免创建过多的session浪费内存)
        // HttpSession session = request.getSession(false);

        // 获取请求头
        Enumeration<String> enumeration = request.getHeaderNames();
        StringBuffer headers = new StringBuffer();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            headers.append(name).append(":").append(value).append(",");
        }

        // 组装请求uri
        StringBuilder builder = new StringBuilder();
        builder.append(UUID.randomUUID()).append("|").append(request.getRequestURI());
        String reqURL = builder.toString();
        key.set(reqURL);

        // 获取请求参数
        String reqMethod = request.getMethod();
        StringBuffer params = new StringBuffer();
        if (HttpMethod.GET.toString().equals(reqMethod)) {
            // get请求
            String queryString = request.getQueryString();
            if (StringUtils.isNotBlank(queryString)) {
                params.append(queryString);
            }
        } else {
            // 其他请求
            Object[] paramsArr = joinPoint.getArgs();
            if (null != paramsArr && paramsArr.length > 0) {
                for (Object arg : paramsArr) {
                    // null值处理
                    if (null == arg) {
                        continue;
                    }
                    // 文件拦截，避免打印文件日志，过多占用空间
                    if (arg instanceof ServletRequest || arg instanceof ServletResponse ||
                        arg instanceof MultipartFile) {
                        log.warn("【HttpLogAspect切面, 拦截到ServletRequest、ServletResponse或文件入参, 不做log打印】");
                        continue;
                    }
                    // 参数组装
                    if (arg instanceof Serializable) {
                        params.append(arg.toString()).append(",");
                    } else {
                        // 使用json系列化 反射等等方法 反系列化会影响请求性能建议重写tostring方法实现系列化接口
                        try {

                            String param = objectMapper.writeValueAsString(arg);
                            params.append(param).append(",");
                        } catch (JsonProcessingException e) {
                            log.error("【请求切面错误】doBefore obj to json exception", e);
                        }
                    }
                }
            }
        }
        // 获取请求ip地址、请求信息打印
        String ip = ClientAccessIpUtil.getClientIpAddr(request);
        log.info("【请求方式和请求头】request method and header, req_method ={}, req_header ={}", reqMethod, headers);
        log.info(
                "【请求链接、请求ip、请求类、请求函数和参数信息】request infomations details, req_URL ={}, req_ip ={}, req_type ={}, req_method ={}, req_params ={}",
                reqURL, ip, joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                params);
    }

    /**
     * 在方法执行后打印返回内容
     *
     * @param obj
     */
    @AfterReturning(returning = "obj", pointcut = "pointcutDefine()")
    public void doAfterReturing(Object obj) {
        // threadLocal内存释放
        long costTime = System.currentTimeMillis() - startTime.get();
        startTime.remove();
        String uri = key.get();
        if (null != uri) {
            key.remove();
        }

        // 获取result
        if (null == obj) {
            log.warn("【HttpLogAspect切面,后置通知获取return结果为null】costTime ={} ms", costTime);
            return;
        }

        String result = null;
        if (obj instanceof Serializable) {
            result = obj.toString();
        } else {
            try {
                result = objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                log.error("doAfterReturing obj to json exception, obj={}", obj, e);
            }
        }
        log.info("【响应结果】response infomations, costTime ={} ms , uri ={}, result ={}", costTime, uri, result);
    }
}
