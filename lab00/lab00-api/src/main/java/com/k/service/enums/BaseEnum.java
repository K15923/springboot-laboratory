package com.k.service.enums;

/**
 * @author k 2022/8/22 20:39
 */
public interface BaseEnum<T extends Enum<T> & BaseEnum<T>> {

    static <T extends Enum<T> & BaseEnum<T>> T parseByCode(Class<T> clazz, Integer code) {
        Enum[] enumConstants = clazz.getEnumConstants();
        for (Enum enumConstant : enumConstants) {
            if (((BaseEnum) enumConstant).getCode().equals(code)) {
                return (T) enumConstant;
            }
        }
        return null;
    }

    Integer getCode();

    String getName();

}
