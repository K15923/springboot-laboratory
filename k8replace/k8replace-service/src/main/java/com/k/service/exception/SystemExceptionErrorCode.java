package com.k.service.exception;

import lombok.Getter;
import org.apache.log4j.spi.ErrorCode;

/**
 * 系统异常 : 呈现用户层的异常提示
 *
 * @author wangqing
 * @date: 2022/12/05
 **/
@Getter
public enum SystemExceptionErrorCode implements ErrorCode {
    /**
     * 系统应用异常: 中台系统异常/业务前端系统异常
     */
    SYSTEM_APPLICATION_EXCEPTION(-1, "服务器开小差儿了,请稍后重试！"),

    /**
     * 系统运行时位置异常
     */
    SYSTEM_UNKNOWN_ERROR(999999, "系统异常,请联系管理员处理");

    /**
     * 异常码
     */
    private Integer code;
    /**
     * 异常信息
     */
    private String message;

    SystemExceptionErrorCode(int code, String msg) {
        this.code = code;
        this.message = msg;
    }


}
