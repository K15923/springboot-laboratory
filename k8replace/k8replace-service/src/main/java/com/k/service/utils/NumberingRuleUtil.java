package com.k.service.utils;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author liq
 * @created 2014-12-18
 */
public class NumberingRuleUtil {
    /**
     * 生成订单编号
     *
     * @param codeHeader 团购：T；商城：M；机票：A；鲜花：F；酒店：H
     * @return
     */
    public static String newOrdercode(String codeHeader) {
        String[] code = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String rtn = codeHeader + code[DatetimeUtil.getYear() - 2013] + code[DatetimeUtil.getMonth()] +
                     code[DatetimeUtil.getDay()] + code[DatetimeUtil.getHour()] + DatetimeUtil.getMinAndSec() +
                     getRandomStr(4);
        return rtn;
    }

    /**
     * 生成随机数 并且以字符串形式返回
     *
     * @param i 需要的数字长度
     * @return
     */
    private static String getRandomStr(int i) {
        String randomStr = "";
        while (randomStr.length() < i) {
            randomStr += getRandomNumber();
        }
        return randomStr;
    }

    public static String getRandomNumber() {
        return new Double(Math.random() * 10d).intValue() + "";
    }

    /**
     * 生成订单编号
     *
     * @param codeHeader 团购：T；商城：M；机票：A；鲜花：F；酒店：H
     * @return 前缀+用户编号后3位+年份+月份编码+日期编码+小时编码+分秒+随机数
     */
    public static String newOrdercode(String codeHeader, String userId) {
        String[] code = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        if (codeHeader.isEmpty() || userId.isEmpty()) {
            // throw new YosarRuntimeException("生成订单号失败，订单号前缀或用户编号为空。");
            return null;
        }
        String userCode = "";
        switch (userId.length()) {
            case 1:
                userCode = "00" + userId;
                break;
            case 2:
                userCode = "0" + userId;
                break;
            case 3:
                userCode = userId;
                break;

            default:
                userCode = userId.substring(userId.length() - 3, userId.length());
                break;
        }
        String rtn = codeHeader + userCode + code[DatetimeUtil.getYear() - 2013] + code[DatetimeUtil.getMonth()] +
                     code[DatetimeUtil.getDay()] + code[DatetimeUtil.getHour()] + DatetimeUtil.getMinAndSec() +
                     (int) (Math.random() * 10000);
        return rtn;
    }

    /**
     * 生成基础编号
     *
     * @param codeHeader 前缀
     * @return 前缀+年份+月份编码+日期编码+小时编码+分秒+随机数
     */
    public static String newBaseCode(String codeHeader) {
        String[] code = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String rtn = codeHeader + DatetimeUtil.getYear() + code[DatetimeUtil.getMonth()] + code[DatetimeUtil.getDay()] +
                     code[DatetimeUtil.getHour()] + DatetimeUtil.getMinAndSec() + (int) (Math.random() * 10000);
        return rtn;
    }

    /**
     * 生成自定义编号
     *
     * @param codeHeader 前缀
     * @param codeCenter 中间码
     * @param codeEnd    后缀
     * @param ifNeedTime 是否需要日期时间编码，true返回 ：年份+月份编码+日期编码+小时编码+分秒+随机数
     * @return 前缀+中间码+随机数/时间码+后缀
     */
    public static String newBaseCode(String codeHeader, String codeCenter, String codeEnd, boolean ifNeedTime) {
        String unique = "";
        if (ifNeedTime) {
            String[] code = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
            unique = DatetimeUtil.getYear() + code[DatetimeUtil.getMonth()] + code[DatetimeUtil.getDay()] +
                     code[DatetimeUtil.getHour()] + DatetimeUtil.getMinAndSec() + (int) (Math.random() * 10000);
        }

        String rtn = codeHeader + codeCenter + unique + codeEnd;
        return rtn;
    }

    /**
     * 生成自定义编号
     *
     * @param codePrefix   前缀
     * @param codeCenter   中间码
     * @param codeSuffix   后缀
     * @param ifNeedTime   是否需要时间
     * @param randomLength 随机数位数
     * @return 前缀+日期时间+中间码+随机数+后缀
     */
    public static String newBaseCode(String codePrefix, boolean ifNeedTime, String codeCenter, int randomLength,
                                     String codeSuffix) {
        String[] code = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String datetimeString = "";
        if (ifNeedTime) {
            datetimeString = DatetimeUtil.getYear() + code[DatetimeUtil.getMonth()] + code[DatetimeUtil.getDay()] +
                             code[DatetimeUtil.getHour()] + DatetimeUtil.getMinAndSec();
        }
        String randomString = "";
        if (randomLength > 0) {
            for (int i = 0; i < randomLength; i++) {
                randomString += getRandomNumber();
            }
        }
        codePrefix = StringUtils.isEmpty(codePrefix) ? "" : codePrefix;
        codeCenter = StringUtils.isEmpty(codeCenter) ? "" : codeCenter;
        codeSuffix = StringUtils.isEmpty(codeSuffix) ? "" : codeSuffix;
        return codePrefix + datetimeString + codeCenter + randomString + codeSuffix;
    }

    /**
     * 生成自定义编号
     *
     * @param codeHeader 前缀
     * @param timeStamp  是否需要日期时间编码，true返回 ：年份+月份编码+日期编码+小时编码+分秒+随机数
     * @return 前缀+中间码+随机数/时间码+后缀
     */
    public static String newBaseCode(String codeHeader, Long timeStamp) {
        Date date = new Date(timeStamp);
        String[] code = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String time = DatetimeUtil.getYear(date) + code[DatetimeUtil.getMonth(date)] + code[DatetimeUtil.getDay(date)] +
                      code[DatetimeUtil.getHour(date)] + DatetimeUtil.getMinAndSec(date) +
                      (int) (Math.random() * 10000);

        String rtn = codeHeader + time;
        return rtn;
    }

    public static void main(String[] args) {
        // System.out.println(NumberingRuleUtil.newBaseCode("before", true, "center", 5, "after"));
        // for (int i = 0; i < 10000; i++) {
        // if (newOrdercode("M").length() != 13) {
        // System.out.println(newOrdercode("M"));
        // }
        //
        // }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, +7/*-(int) ((now.getTime()-endTime.getTime())/1000/3600/24)*/);
        System.out.println("时间是:" + sdf.format(calendar.getTime()));

        // 获取时间加一年或加一月或加一天
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);// 设置起时间
        // System.out.println("111111111::::"+cal.getTime());
        cal.add(Calendar.YEAR, 1);// 增加一年
        // cal.add(Calendar.DATE, n);//增加一天
        // cal.add(Calendar.DATE, -10);//减10天
        // cal.add(Calendar.MONTH, n);//增加一个月
        System.out.println("输出::" + cal.getTime());
    }
}
