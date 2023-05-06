package com.k.service.utils;

import java.util.Random;

public class GeneratorUtil {

    /**
     * 根据用户ID获取推荐码
     *
     * @param
     * @return
     */
    public static String getPromotionCode(Long len) {
        // 字符源，可以根据需要删减
        String generateSource = "23456789abcdefghgklmnpqrstuvwxyz";// 去掉1和i ，0和o
        String rtnStr = "";
        for (int i = 0; i < len; i++) {
            // 循环随机获得当次字符，并移走选出的字符
            String nowStr = String.valueOf(
                    generateSource.charAt((int) Math.floor(Math.random() * generateSource.length())));
            rtnStr += nowStr;
            generateSource = generateSource.replaceAll(nowStr, "");
        }
        return rtnStr.toUpperCase();
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        //        List<String> objects = new ArrayList<>();
        //        for (int i = 1; i < 1000010; i++) {
        //            String s = GeneratorUtil.getPromotionCode(6L).toUpperCase();
        //            System.out.println(s);
        //            objects.add(s);
        //        }
        //        System.out.println(objects.size());
        //        List<String> collect = objects.stream().distinct().collect(Collectors.toList());
        //        System.out.println(collect.size());
        System.out.println(getRandomNumber(100000, 999999));

    }

    /**
     * 得到from到to的随机数，包括to
     *
     * @param from
     * @param to
     * @return
     */
    public static int getRandomNumber(int from, int to) {
        float a = from + (to - from) * (new Random().nextFloat());
        int b = (int) a;
        return ((a - b) > 0.5 ? 1 : 0) + b;
    }
}