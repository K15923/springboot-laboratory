package com.k.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt.config")
public class JwtProperties {
    /**
     * 过期时间
     */
    private long expireTimeMills;
    /**
     * jwt 密钥
     */
    private String jwtSecret;

    /**
     * JWT签发者
     */
    private String jwtIss;

    /**
     * token头
     */
    private String tokenHead;

    /**
     * 头名称
     */
    private String header;
}