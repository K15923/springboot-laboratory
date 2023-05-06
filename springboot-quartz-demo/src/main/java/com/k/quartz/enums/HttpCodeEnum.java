package com.k.quartz.enums;

/**
 * @author k 2023/4/18 9:36
 */
public enum HttpCodeEnum {

    /**
     * 成功
     */
    SUCCESS(0, "成功"),
    /**
     * 失败
     */
    ERROR(-1, "系统错误"),
    /**
     * token过期
     */
    TOKEN_INVALID(9999, "token过期");

    private int index;
    private String value;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    HttpCodeEnum(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public static String getValue(int index) {
        for (HttpCodeEnum dataSourceEnum : HttpCodeEnum.values()) {
            if (dataSourceEnum.getIndex() == index) {
                return dataSourceEnum.getValue();
            }
        }
        return null;
    }
}
