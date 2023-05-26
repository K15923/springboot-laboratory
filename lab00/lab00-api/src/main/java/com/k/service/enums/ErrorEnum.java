package com.k.service.enums;

/**
 * @author k 2023/5/16 15:10
 */
public enum ErrorEnum implements BaseEnum {

    NO_AUTH(900001, "无操作权限");

    private Integer code;
    private String name;

    ErrorEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name + ": " + this.code;
    }
}
