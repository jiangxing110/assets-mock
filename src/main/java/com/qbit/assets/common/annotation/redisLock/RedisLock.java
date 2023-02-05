package com.qbit.assets.common.annotation.redisLock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis分布式锁
 *
 * @author klover
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {
    /**
     * 锁类型
     *
     * @return int
     */
    int lock() default MutexLock;

    /**
     * 设置请求锁定时间(秒),超时后自动释放锁
     *
     * @return int
     */
    long ttl() default 60;


    /**
     * 设置重试请求次数
     *
     * @return int
     */
    int retryCount() default 1;

    /**
     * 设置重试间隔时间 秒
     *
     * @return int
     */
    long retryDelay() default 10;

    /**
     * 锁自定义名称
     *
     * @return
     */
    String name() default "";

    /**
     * 公共锁名称前缀
     */
    String publicName() default "qbitpay:3:redisLock:";

    /**
     * 排他锁
     */
    int MutexLock = 0;

    /**
     * 重试锁
     */
    int BufferLock = 1;

    /**
     * 排他锁 联合锁
     */
    int MutexLocks = 2;

    /**
     * 重试锁 联合锁
     */
    int BufferLocks = 3;
}
