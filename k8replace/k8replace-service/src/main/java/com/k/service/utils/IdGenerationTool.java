package com.k.service.utils;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class IdGenerationTool {

    private static final int rate = 1000;
    private static final int MAX = 1000 * rate;
    private static final UniqueRandom ur = new UniqueRandom(MAX);
    private static final int SIZE = 1000 * rate;
    private static final int NEED = 1000 * rate;
    private static final int[] result;
    private static final int max_length = 30;

    static {
        result = ur.randomList(NEED, SIZE);
    }

    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成预订订单编号
     */
    public static String gengenerationOrderId(String lpn, String cityCode, String code) {
        Date now = new Date();
        String orderId = cityCode + "-" + code + "-" + lpn.substring(1, lpn.length()) + "-" + now.getTime();
        return orderId;
        // return getGengenerationId();
    }

    /**
     * 生成日租订单编号
     */
    public static String gengenerationDayRentOrderId(String cityCode, String code) {
        Date now = new Date();
        String orderId = cityCode + "-" + code + "-" + now.getTime();
        return orderId;
        // return getGengenerationId();
    }

    /**
     * 生成充值订单编号
     *
     * @return
     */
    public static String gengenerationChargeId(String memberId, String chargeType) {
        // String chargeId = "Cr" + chargeType + "r" + memberId + "r" + System.currentTimeMillis();
        // return chargeId;
        return getGengenerationId();
    }

    public static String getGengenerationId() {
        return getGengenerationId("");
    }

    public static String getGengenerationId(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Calendar c = Calendar.getInstance();
        String time = sdf.format(c.getTime());
        String tmpR = type.toUpperCase() + time;
        int length = tmpR.length();
        if (length == max_length) {
            return tmpR;
        } else if (length > max_length) {
            return tmpR.substring(0, max_length);
        } else {
            if (result.length > 0) {
                Random r = new Random();
                int ints = r.nextInt(result.length - 1);
                int tmpInts = result[ints];
                return tmpR + StringUtils.leftPad(String.valueOf(tmpInts), max_length - length, "0");
            } else {
                return StringUtils.rightPad(tmpR, max_length, "0");
            }
        }
    }

    /**
     * 生成退款订单编号
     *
     * @return
     */
    public static String gengenerationRefundId(String customerId) {
        // String chargeId = "R-"+customerId+"-"+System.currentTimeMillis();
        // return chargeId;
        return getGengenerationId();
    }

    /**
     * 生成支付订单编号
     *
     * @return
     */
    public static String gengenerationPayOrderId(String memberId, String chargeType) {
        // String chargeId = "TSr" + chargeType + "r" + memberId + "r" + System.currentTimeMillis();
        // return chargeId;
        return getGengenerationId();
    }

    /**
     * 生成日租订单编号
     */
    public static String gengenerationChargingOrderId(String cityCode, String code, Integer pileId) {
        Date now = new Date();
        String orderId = cityCode + "-" + code + "-" + pileId + "-" + now.getTime();
        return orderId;
    }
}
