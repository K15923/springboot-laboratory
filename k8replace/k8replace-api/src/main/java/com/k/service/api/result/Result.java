package com.k.service.api.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(description = "返回结果信息")
public class Result<T> {
    @ApiModelProperty(value = "接口状态码，正常为0，异常为-1，其他业务异常码等", example = "0")
    private int code;
    @ApiModelProperty(value = "返回接口的信息，正常信息，异常信息等", example = "登录成功")
    private String msg;
    @ApiModelProperty(value = "返回接口的数据")
    private T data;

    public static <T> Result<T> ok(String message, T data) {
        return (Result<T>) Result.builder().code(0).msg(message).data(data).build();
    }

    public static <T> Result<T> ok(T data) {
        return (Result<T>) Result.builder().code(0).msg("操作成功").data(data).build();
    }

    public static <T> Result<T> ok(String message) {
        return (Result<T>) Result.builder().code(0).msg(message).build();
    }

    public static <T> Result<T> ok() {
        return (Result<T>) Result.builder().code(0).msg("操作成功").build();
    }

    public static <T> Result<T> error() {
        return (Result<T>) Result.builder().code(-1).msg("操作失败").build();
    }

    public static <T> Result<T> error(String message) {
        return (Result<T>) Result.builder().code(-1).msg(message).build();
    }

    public static <T> Result<T> error(int code, String message) {
        return (Result<T>) Result.builder().code(code).msg(message).build();
    }

}
