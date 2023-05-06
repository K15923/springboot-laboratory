package com.k.service.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @Description:
 * @Author: zzw
 * @Create:2019/10/12 15:41
 */
public class LocalDateTimeUtils {

    /**
     * 获取时间戳
     *
     * @param localDateTime
     * @param offsetId
     * @return
     */
    public static long getMills(LocalDateTime localDateTime, String offsetId) {
        return localDateTime.toInstant(ZoneOffset.of(offsetId)).toEpochMilli();
    }

    /**
     * 获取时间localDateTime
     *
     * @param dateTimeStr
     * @param formatter
     * @return
     */
    public static LocalDateTime getLocalDateTime(String dateTimeStr, String formatter) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
        return LocalDateTime.parse(dateTimeStr, df);
    }

    /**
     * 获取二个localDateTime相差几天
     *
     * @param localDateTime1
     * @param localDateTime2
     * @return
     */
    public static int getIntervalDay(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        if (localDateTime1.isAfter(localDateTime2)) {
            return (int) (localDateTime1.toLocalDate().toEpochDay() - localDateTime2.toLocalDate().toEpochDay());
        } else {
            return (int) (localDateTime2.toLocalDate().toEpochDay() - localDateTime1.toLocalDate().toEpochDay());
        }
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp, String offsetId) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneOffset.of(offsetId));
    }

}
