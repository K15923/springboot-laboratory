package com.k.service.utils;

/**
 * 任意进制数的相互转换
 *
 * @author tianchao
 * @date 2019-09-03 9:23
 */
public class ConvertDecimalUtils {

    /**
     * 任意进制数的相互转换。基本原理是先将原来的数转换为10进制数，再转换为新的进制数
     *
     * @param number                原来的数
     * @param originalBase          原来的数的进制
     * @param newBase               需要转换成的进制
     * @param usingUppercaseLetters 是否返回字母全大写的字符串
     * @return 新的进制数（String类型）或“无法转换”的警告
     * @author tc
     */
    public static String baseConverter(int number, int originalBase, int newBase, boolean usingUppercaseLetters) {
        try {
            String originalNumber = Integer.toString(number);
            String newNumber = Integer.toString(Integer.valueOf(originalNumber, originalBase), newBase);
            return usingUppercaseLetters ? newNumber.toUpperCase() : newNumber.toLowerCase();
        } catch (NumberFormatException e) {
            return "Unable to identify: " + number + " cannot be written in base-" + originalBase + " notation.";
        }
    }
}
