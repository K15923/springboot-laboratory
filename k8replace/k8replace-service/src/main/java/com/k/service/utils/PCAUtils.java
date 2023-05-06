package com.k.service.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 获取省、市、区 的 code
 *
 * @author k 2022/8/30 16:38
 */
@Slf4j
public class PCAUtils {

    public static void main(String[] args) {
        String provinceCode = getProvinceCode("440304");
        String cityCode = getCityCode("440304");
        System.out.println(provinceCode);
        System.out.println(cityCode);
    }

    public static String getProvinceCode(String adcode) {
        log.info("PCAUtils获取省份入参adcode:{}", adcode);
        Integer i = Integer.valueOf(adcode) / 10000 * 10000;
        return i.toString();
    }

    public static String getCityCode(String adcode) {
        log.info("PCAUtils获取城市入参adcode:{}", adcode);
        Integer i = Integer.valueOf(adcode) / 100 * 100;
        return i.toString();
    }


}
