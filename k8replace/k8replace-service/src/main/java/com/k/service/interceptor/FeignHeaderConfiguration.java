package com.k.service.interceptor;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wangqing
 * @DATE: 2022/12/6
 */
@Configuration
public class FeignHeaderConfiguration implements RequestInterceptor {
    @Bean
    Logger.Level loggerLevel() {
        // 这里记录所有，根据实际情况选择合适的日志level
        return Logger.Level.FULL;
    }

    @Bean
    Logger feignLogger() {
        return new FeignLogger();
    }

    @Override
    public void apply(RequestTemplate template) {

    }
}
