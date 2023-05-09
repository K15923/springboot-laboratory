package com.k.lab01mybatis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TableName t_user_info
 */
@TableName(value = "t_user_info")
@Data
@Builder
// @AllArgsConstructor
// @NoArgsConstructor
public class UserInfo implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * app
     */
    @TableField(value = "app")
    private Integer app;

    /**
     * 版本
     */
    @TableField(value = "version")
    private String version;

    /**
     * city
     */
    @TableField(value = "city")
    private String city;

    /**
     * province
     */
    @TableField(value = "province")
    private String province;

    /**
     * area
     */
    @TableField(value = "area")
    private String area;

    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 手机号码
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}