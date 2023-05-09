package com.k.lab01mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.k.lab01mybatis.dao.mapper")
public class Lab01MybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab01MybatisApplication.class, args);
    }

}
