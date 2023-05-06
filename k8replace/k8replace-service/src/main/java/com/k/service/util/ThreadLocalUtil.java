package com.k.service.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.k.service.api.dto.QueryMerchandiseDto;

/**
 * @author: xujiacong@ejiayou.com
 * @time: 2022-08-03  16:43
 * @description:
 */
public class ThreadLocalUtil {

    private static final TransmittableThreadLocal<QueryMerchandiseDto> CURRENT_USER = new TransmittableThreadLocal<>();

    public static QueryMerchandiseDto getCurrentUser() {
        if (CURRENT_USER.get() != null) {
            return CURRENT_USER.get();
        } else {
            throw new RuntimeException();
        }
    }

    public static void setCurrentUser(QueryMerchandiseDto member) {
        CURRENT_USER.set(member);
    }

    public static QueryMerchandiseDto getCurrentUser(Boolean checkLogin) {
        if (CURRENT_USER.get() != null) {
            return CURRENT_USER.get();
        }
        if (checkLogin) {

        }
        return null;
    }

    public static void remove() {
        CURRENT_USER.remove();
    }
}
