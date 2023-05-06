package com.k.service.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * @author: chenqingyu
 * @date: 2022/9/27 16:04
 **/
public class JsonUtils {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = SpringContextUtils.getBean(ObjectMapper.class);
    }

    @SneakyThrows
    public static String toJson(Object obj) {
        ObjectMapper objectMapper = SpringContextUtils.getBean(ObjectMapper.class);
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T toBean(String json, Class<T> obj) {
        return objectMapper.readValue(json, obj);
    }

    @SneakyThrows
    public static String toJsonWithDefaultPretty(Object o) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }
}
