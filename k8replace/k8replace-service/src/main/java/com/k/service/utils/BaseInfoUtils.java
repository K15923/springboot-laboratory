package com.k.service.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by MyPC on 2019/6/12.
 */
public class BaseInfoUtils {

    public static void kv(String key, Object value, StringBuffer stringBuffer) {
        stringBuffer.append(":");
        if (value instanceof List) {
            List<Object> objects = (List<Object>) value;
            for (Object object : objects) {
                Map<String, Object> objectMap = ModelConvert.beanToMap(object);
                for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                    kv(entry.getKey(), entry.getValue(), stringBuffer);
                }
            }
        } else if (value instanceof Set) {
            Set<Object> objects = (Set<Object>) value;
            stringBuffer.append(key);
            for (Object object : objects) {
                stringBuffer.append("_").append(object);
            }
        } else {
            stringBuffer.append(key).append("_").append(value);
        }
    }

}
