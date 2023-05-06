package com.k.service.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 集合相关处理工具类
 */
public class Commons {

    /**
     * 判断某个map是否不为空
     *
     * @param m
     * @return
     */
    public static boolean isNotEmpty(Map m) {
        return !isEmpty(m);
    }

    /**
     * 判断某个map是否为空
     *
     * @param m
     * @return
     */
    public static boolean isEmpty(Map m) {
        return m == null || m.isEmpty();
    }

    public static boolean isNotEmpty(Object[] args) {
        return !isEmpty(args);
    }

    public static boolean isEmpty(Object[] args) {
        return args == null || args.length == 0;
    }

    /**
     * 判断某个集合是否不为空
     *
     * @param coll
     * @return
     */
    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    /**
     * 判断某个集合是否为空
     *
     * @param c
     * @return
     */
    public static boolean isEmpty(Collection c) {
        return CollectionUtils.isEmpty(c);
    }

    /**
     * 判断对象是否为NotEmpty(!null或元素>0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Object pObj) {
        return !isEmpty(pObj);
    }

    /**
     * 判断对象是否Empty(null或元素为0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param obj 待检查对象
     * @return boolean 返回的布尔值
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            return StringUtils.isBlank(obj.toString());
        } else if (obj instanceof Collection) {
            return isEmpty(((Collection) obj));
        } else if (obj instanceof Map) {
            return isEmpty((Map) obj);
        }
        return false;
    }

}
