package com.k.lab02redis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * app
     */
    private Integer app;

    /**
     * 版本
     */
    private String version;

    /**
     * city
     */
    private String city;

    /**
     * province
     */
    private String province;

    /**
     * area
     */
    private String area;

    /**
     * 地址
     */
    private String address;

    /**
     * 手机号码
     */
    private String phoneNumber;

}