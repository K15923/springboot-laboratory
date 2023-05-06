package com.k.quartz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author k 2023/4/17 13:34
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "linuxenv")
public class Env {
    private String localhost;
    private String localusername;
    private String localpassword;
    private String remotehost;
    private String remotport;
    private String remoteusername;
    private String remotepassword;
}
