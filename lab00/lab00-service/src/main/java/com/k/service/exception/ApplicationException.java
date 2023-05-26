package com.k.service.exception;

import com.k.service.enums.ErrorEnum;

/**
 * 应用错误 : 调用依赖系统异常
 * 熔断/异常状态/数据为空
 */
public class ApplicationException extends RuntimeException {
    /**
     * 错误code码
     */
    private Integer code;

    /**
     * 错误提示消息
     */
    private String message;

    /**
     * 构造函数
     *
     * @param errorEnum
     */
    public ApplicationException(ErrorEnum errorEnum) {
        super();
        this.code = errorEnum.getCode();
        this.message = errorEnum.getName();
    }

    /**
     * Returns this code object.
     *
     * @return this code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Sets this code.
     *
     * @param code this code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * Returns this message object.
     *
     * @return this message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Sets this message.
     *
     * @param message this message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}