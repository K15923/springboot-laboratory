package com.k.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * description:入口类 Author Date Changes tc 2020-05-07 Created
 */
@SpringBootApplication
// @EnableDiscoveryClient
// @EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.k"})
public class Lab00Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab00Application.class, args);
    }

    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

}
