package com.qbit.assets.common.annotation.currentUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录检测注解
 *
 * @author EDZ
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoAuth {
    int value() default NO_LOGIN;

    /**
     * 无需登录
     */
    int NO_LOGIN = 0;

    /**
     * 内部调用 会校验签名
     */
    int INTERNAL = 1;

    /**
     * 普通调用 会校验登录token
     */
    int LOGIN = 2;
}
