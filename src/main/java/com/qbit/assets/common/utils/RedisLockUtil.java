package com.qbit.assets.common.utils;


import com.qbit.assets.common.error.CustomException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author klover
 */
@Component
public class RedisLockUtil {
    @Resource
    private RedissonClient redissonClient;

    /**
     * （Reentrant Lock），属于非公平锁。
     * 假设当前持有锁的线程是线程A，CHL队列中有线程B、线程C、…、线程N在排队，当线程A再次竞争资源时，可直接获得锁。
     * 也即需要获取锁的线程与当前正在持有锁的线程是同一个时，可以直接获取锁，无须排队。
     *
     * @param name 锁名称
     * @return RLock
     */
    public RLock reentrantLock(String name) {
        return redissonClient.getLock(name);
    }


    /**
     * 公平锁（Fair Lock）
     * 在提供了自动过期解锁功能的同时，保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
     *
     * @param name 锁名称
     * @return RLock
     */
    public RLock fairLock(String name) {
        return redissonClient.getFairLock(name);
    }

    /**
     * 组合锁
     *
     * @param locks 锁
     * @return RLock
     */
    public RLock multiLock(List<RLock> locks) {
        return redissonClient.getMultiLock(locks.toArray(new RLock[]{}));
    }

    /**
     * 解锁
     *
     * @param lock lock
     */
    public void unLock(RLock lock) {
        try {
            lock.unlock();
        } catch (Throwable ignored) {

        }
    }

    /**
     * 可重入锁 加锁 同线程下可以同时获取到锁
     *
     * @param lock      lock
     * @param waitTime  等待获取锁时间 秒
     * @param leaseTime 锁过期时间 秒
     * @return boolean
     * @throws InterruptedException error
     */
    public Boolean setLock(RLock lock, long waitTime, long leaseTime) throws InterruptedException {
        return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
    }

    /**
     * 排他锁
     *
     * @param lock 锁
     * @param ttl  锁过期时间 秒
     * @return boolean
     */
    public Boolean mutexLock(RLock lock, long ttl) throws Throwable {
        try {
            ttl = ttl > 0 ? ttl : 60;
            return this.setLock(lock, 0, ttl);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 重试锁
     *
     * @param lock       锁
     * @param ttl        锁过期时间 秒
     * @param retryCount 设置重试请求次数
     * @param retryDelay 设置重试间隔时间 秒
     * @return boolean
     */
    public boolean bufferLock(RLock lock, long ttl, int retryCount, long retryDelay) throws Throwable {
        int i = 1;
        retryCount = retryCount > 0 ? retryCount : i;
        retryDelay = retryDelay > 0 ? retryDelay : 10;
        ttl = ttl > 0 ? ttl : 60;

        try {
            do {
                // 判断是否已经上锁
                boolean isLockExist = lock.isLocked();
                if (!isLockExist) {
                    boolean res = this.mutexLock(lock, ttl);
                    if (res) {
                        return true;
                    }
                }
                ++i;

                // 暂停当前线程
                if (retryCount >= i) {
                    Thread.sleep(retryDelay * 1000L);
                }
            } while (retryCount >= i);
            throw new CustomException("任务太多，请稍后重试");
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * 重试锁 联合锁
     *
     * @param locks      锁
     * @param multiLock  联合锁
     * @param ttl        锁过期时间 秒
     * @param retryCount 设置重试请求次数
     * @param retryDelay 设置重试间隔时间 秒
     * @return boolean
     */
    public boolean bufferLocks(List<RLock> locks, RLock multiLock, long ttl, int retryCount, long retryDelay) throws Throwable {
        int i = 1;
        retryCount = retryCount > 0 ? retryCount : i;
        retryDelay = retryDelay > 0 ? retryDelay : 10;
        ttl = ttl > 0 ? ttl : 60;


        try {
            do {
                // 判断是否已经上锁
                boolean isLockExist = false;
                for (RLock lock : locks) {
                    isLockExist = lock.isLocked();
                    if (isLockExist) {
                        break;
                    }
                }

                if (!isLockExist) {
                    boolean res = this.mutexLocks(multiLock, ttl, retryCount);
                    if (res) {
                        return true;
                    }
                }
                ++i;
                // 暂停当前线程
                if (retryCount > i) {
                    Thread.sleep(retryDelay * 1000L);
                }
            } while (retryCount > i);
            throw new CustomException("任务太多，请稍后重试");
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 排他锁 联合锁
     *
     * @param lock 锁
     * @param ttl  锁过期时间 秒
     * @return boolean
     */
    public boolean mutexLocks(RLock lock, long ttl, int retryCount) throws Throwable {
        try {
            ttl = ttl > 0 ? ttl : 60;
            // 加锁执行时间 必须给
            long waitTime = retryCount * 3L;
            return this.setLock(lock, waitTime, ttl);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }
}
