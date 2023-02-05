package com.qbit.assets.service;

/**
 * redis服务
 * tips: 键值对封装
 *
 * @author litao
 */
public interface RedisService {
    /**
     * set存数据
     *
     * @param key   key
     * @param value value
     * @return ret
     */
    boolean set(String key, String value);

    /**
     * set存数据
     *
     * @param key    key
     * @param value  value
     * @param expire ttl
     * @return ret
     */
    boolean set(String key, String value, long expire);

    /**
     * key是否存在
     *
     * @param key key
     * @return boolean
     */
    boolean hasKey(String key);

    /**
     * get获取数据
     *
     * @param key key
     * @return value
     */
    String get(String key);

    /**
     * 设置有效期
     *
     * @param key    key
     * @param expire 有效期(秒戳)
     * @return ret
     */
    boolean expire(String key, long expire);

    /**
     * 移除数据
     *
     * @param key key
     * @return ret
     */
    boolean remove(String key);

    /**
     * 获取自增1后的 值
     *
     * @param key  key
     * @param time time(秒戳)
     * @return value
     */
    long increment(String key, long time);
}
