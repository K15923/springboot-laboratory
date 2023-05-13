package com.k.quartz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author k 2023/4/17 13:45
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "db")
public class Dbenv {
    private String databaseName;
    private String username;
    private String password;
    private String host;
    private String port;
}
