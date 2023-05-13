package com.k.quartz.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author k 2023/4/18 9:12
 */
@Data
public class LoginVO {

    @ApiModelProperty(value = "token", example = "ashjdgkjl")
    private String token;

    @ApiModelProperty(value = "adminUser", example = "admin")
    private String adminUser;

    @ApiModelProperty(value = "timestamp", example = "151352135L")
    private Long timestamp;

}
