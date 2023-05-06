package com.k.service.config;

/**
 * 常量配置类
 */
public class ConstantConfig {

    /**
     * 油品码表key
     */
    public final static String OIL_TYPE_CODE_KEY = "oilType";

    /**
     * 油站品牌码表key
     */
    public final static String BRAND_NAME_CODE_KEY = "brandName";

    /**
     * 油站坐标key
     */
    public final static String STATION_COORDINATE_KEY = "STATION_COORDINATE";

    /**
     * 油站信息
     */
    public final static String STATION_INFO_KEY = "STATION_INFO";

    /**
     * 首页油站查询范围，单位KM
     */
    public final static Double SEARCH_RANGE = 50000.0;

    /**
     * 油站首页查询最低价，油站范围，单位KM
     */
    public final static Double LOWEST_PRICE_RANGE = 5000.0;

    /**
     * redis中用户手机号
     */
    public static final String UCS_USER_ID_PREFIX = "galaxy_ucs:phone_id:";


}
