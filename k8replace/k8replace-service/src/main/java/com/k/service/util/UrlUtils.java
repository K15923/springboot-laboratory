package com.k.service.util;

/**
 * Created by GuoBin on 2019-04-23.
 */
public class UrlUtils {
    /**
     * 移除 URI 中多余的的斜线
     *
     * @param uri uri
     * @return uri
     */
    public static String removeExtraSlashOfUri(String uri) {
        if (uri == null || uri.length() == 0) {
            return uri;
        }
        return uri.replaceAll("^/*|/*$", "");
    }
}
