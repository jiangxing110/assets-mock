package com.qbit.assets.common.aspect;


import com.qbit.assets.common.annotation.redisLock.LockKey;
import com.qbit.assets.common.annotation.redisLock.RedisLock;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * redis 分布式锁处理接口幂等性
 */
@Order(value = 2) // AOP执行顺序
@Slf4j
@Aspect
@Component
public class RedisLockAspect {
    @Resource
    private RedisLockUtil redisLockUtil;

    @Pointcut("@annotation(com.qbit.assets.common.annotation.redisLock.RedisLock)")
    public void redisLock() {
    }

    @Around("redisLock()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if (signature == null) {
            return joinPoint.proceed();
        }
        Method method = signature.getMethod();
        if (method == null) {
            return joinPoint.proceed();
        }

        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        if (redisLock == null) {
            return joinPoint.proceed();
        }

        // 获取锁方式
        int lockType = redisLock.lock();
        // 获取锁过期时间
        long ttl = redisLock.ttl();
        // 如果为排它锁  只能执行一次
        int retryCount = (lockType == 0 || lockType == 2) ? 1 : redisLock.retryCount();
        long retryDelay = redisLock.retryDelay();

        // 获取注解参数
        Map<String, String> paramsMap = this.getAnnotationParams(method, joinPoint);

        RLock lock = null;
        try {
            if (lockType == 2 || lockType == 3) {

                // 联合锁
                List<RLock> locks = this.getLocks(redisLock, paramsMap);
                lock = redisLockUtil.multiLock(locks);
                redisLockUtil.bufferLocks(locks, lock, ttl, retryCount, retryDelay);
            } else {
                // 普通锁
                lock = this.getLock(redisLock, paramsMap);
                redisLockUtil.bufferLock(lock, ttl, retryCount, retryDelay);
            }
            return joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        } finally {
            redisLockUtil.unLock(lock);
        }
    }

    /**
     * 获取锁
     *
     * @param redisLock 锁
     * @return RLock
     */
    public RLock getLock(RedisLock redisLock, Map<String, String> paramsMap) {

        // redis 前缀
        String publicName = redisLock.publicName();
        StringBuilder name = new StringBuilder(redisLock.name());
        name.insert(0, publicName);

        for (String key : paramsMap.keySet()) {
            if (paramsMap.get(key) == null) {
                continue;
            }
            name.append(key).append(":").append(paramsMap.get(key));
        }

        return redisLockUtil.reentrantLock(name.toString());
    }

    /**
     * 获取锁 联合锁
     *
     * @param redisLock 锁
     * @return RLock
     */
    public List<RLock> getLocks(RedisLock redisLock, Map<String, String> paramsMap) {

        // redis 前缀
        String publicName = redisLock.publicName();

        List<RLock> list = new ArrayList<>();
        ArrayList<String> arr = new ArrayList<>();
        for (String key : paramsMap.keySet()) {
            if (paramsMap.get(key) == null) {
                continue;
            }
            arr.add(paramsMap.get(key));
        }
        // 排序
        Collections.sort(arr);

        String name = redisLock.name();
        for (String s : arr) {
            String currentName = publicName + name + s;
            RLock lock = redisLockUtil.reentrantLock(currentName);
            list.add(lock);
        }

        return list;
    }

    /**
     * 获取注解参数
     *
     * @return Map<String, String>
     */
    private Map<String, String> getAnnotationParams(Method method, ProceedingJoinPoint joinPoint) throws Exception {
        //获取请求参数
        Object[] objects = joinPoint.getArgs();

        // 获取注解参数
        Annotation[][] annotations = method.getParameterAnnotations();
        Map<String, String> paramsMap = new TreeMap<>();
        for (int i = 0; i < annotations.length; i++) {
            this.getSubAnnotationParams(annotations[i], i, objects, paramsMap);
        }
        return paramsMap;
    }

    /**
     * 获取注解参数 子集循环
     */
    private void getSubAnnotationParams(Annotation[] annotation, int i, Object[] objects, Map<String, String> paramsMap) throws Exception {
        for (Annotation item : annotation) {
            if (!(item instanceof LockKey myRequestParameter)) {
                continue;
            }
            String[] keys = myRequestParameter.value();
            if (objects[i] instanceof String || objects[i] instanceof Number || objects[i] instanceof Boolean) {
                if (keys.length <= 0) {
                    paramsMap.put("" + i, "" + objects[i]);
                }
                for (String key : keys) {
                    paramsMap.put(key, "" + objects[i]);
                }

            } else {
                Object obj = objects[i];
                // 获得对象的类型
                Class<?> classType = obj.getClass();
                Field[] fields1 = classType.getDeclaredFields();
                Class<?> superClassType = classType.getSuperclass();
                Field[] fields2 = superClassType.getDeclaredFields();

                Field[] fields = ArrayUtils.addAll(fields1, fields2);
                Map<String, Object> map = new HashMap<>();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    String firstLetter = fieldName.substring(0, 1).toUpperCase();
                    String getMethodName = "get" + firstLetter + fieldName.substring(1);
                    Method getMethod = classType.getMethod(getMethodName);
                    Object value = getMethod.invoke(obj);
                    map.put(fieldName, value);
                }
                if (keys.length <= 0) {
                    throw new CustomException("参数注解有误,请指定key");
                }
                for (String key : keys) {
                    paramsMap.put(key, (String) map.get(key));
                }
            }


        }
    }
}
