package com.qbit.assets.common.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author litao
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取当前环境
     *
     * @return env
     */
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

    public static boolean isProd() {
        return "prod".equals(getActiveProfile());
    }

    /**
     * 获取applicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * 通过Annotation获取 Bean.
     *
     * @param clazz 注解类
     * @return Map<String, Object>
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> clazz) {
        return context.getBeansWithAnnotation(clazz);
    }

    /**
     * 获取注解的类名
     *
     * @param clazz 注解类
     * @return String[]
     */
    public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> clazz) {
        return context.getBeanNamesForAnnotation(clazz);
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name 名称
     * @return Object
     */
    public static Object getBean(String name) {
        return context.getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz 类
     * @param <T>   类
     * @return <T>
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

}
