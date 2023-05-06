package com.k.service.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * BigDecimal 类型转换工具
 */
public class BigDecimalUtil {
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * 展示两位小数:小数位不够补0
     *
     * @param bigDecimal 入参
     * @return 两位小数出参
     */
    public static BigDecimal round(BigDecimal bigDecimal) {
        if (Objects.isNull(bigDecimal)) {
            return null;
        }
        return bigDecimal.setScale(2, RoundingMode.DOWN);
    }
}
