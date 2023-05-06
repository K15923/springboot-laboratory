package com.k.quartz.enums;

/**
 *  备份周期
 *  3天、7天、15天、30天、120天
 */
public enum BackCycleEnum {

    THREE_DAYS(3),
    SEVEN_DAYS(7),
    FIFTEEN_DAYS(15),
    THIRTY_DAYS(30),
    ONE_HUNDRED_TWENTY_DAYS(120);

    private final int days;

    BackCycleEnum(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public static BackCycleEnum getDefault() {
        return SEVEN_DAYS;
    }

}
