package com.k.service.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import static cn.hutool.extra.spring.SpringUtil.getActiveProfile;

@Component
public class SpringContextUtils implements ApplicationListener<ContextRefreshedEvent> {

    private static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> cls) {
        if (applicationContext == null) {
            throw new RuntimeException("applicationContext注入失败");
        }
        return applicationContext.getBean(cls);
    }

    public static Object getBean(String name) {
        if (applicationContext == null) {
            throw new RuntimeException("applicationContext注入失败");
        }
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> cls) {
        if (applicationContext == null) {
            throw new RuntimeException("applicationContext注入失败");
        }
        return applicationContext.getBean(name, cls);
    }

    public static ApplicationContext get() {
        return applicationContext;
    }

     public static boolean isOfficeProfile() {
        return "office".equals(getActiveProfile());
    }

    public static boolean isDevProfile() {
        return "test".equals(getActiveProfile()) || "dev".equals(getActiveProfile());
    }

    public static boolean isPreProfile() {
        return "pre".equals(getActiveProfile());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        SpringContextUtils.applicationContext = contextRefreshedEvent.getApplicationContext();
    }
}
