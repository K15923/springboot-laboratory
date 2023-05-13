package com.k.service.config;

import com.k.service.interceptor.AuthInterceptor;
import com.k.service.interceptor.OpenApiInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor interceptor;

    @Resource
    private OpenApiInterceptor openApiInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor(openApiInterceptor).addPathPatterns("/openApi", "/openApi/**")
                .excludePathPatterns("/login", "/swagger-resources**", "**.js", "**.css", "/doc.html**")
                .excludePathPatterns("/webjars/**", "/v2/**");


        registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns("/**");

    }


}
