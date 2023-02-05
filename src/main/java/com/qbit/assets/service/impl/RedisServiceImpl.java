package com.qbit.assets.service.impl;


import com.qbit.assets.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redis服务实现
 *
 * @author litao
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private ValueOperations<String, String> opsForValue() {
        return redisTemplate.opsForValue();
    }


    @Override
    public boolean set(String key, String value) {
        opsForValue().set(key, value);
        return true;
    }

    @Override
    public boolean set(String key, String value, long expire) {
        boolean ret = set(key, value);
        if (!ret) {
            return false;
        }
        expire(key, expire);
        return true;
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public String get(String key) {
        // 使用了自定义的序列化, 可能会把数据序列化成其它数据类型
        // 验证码中踩过坑
        Object code = opsForValue().get(key);
        if (code == null) {
            return null;
        }
        return code.toString();
    }

    @Override
    public boolean expire(String key, long expire) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, expire, TimeUnit.SECONDS));
    }

    @Override
    public boolean remove(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public long increment(String key, long time) {
        Long count = opsForValue().increment(key, 1);
        if (count == null) {
            count = 1L;
        }
        if (count == 1L) {
            //设置有效期一分钟
            expire(key, time);
        }
        return count;
    }
}
