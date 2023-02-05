package com.qbit.assets.common.aspect;


import com.qbit.assets.common.annotation.AccessLimit;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.service.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(value = 1)
public class AccessLimitAspect {
    private static final String ACCESS_LIMIT_KEY = "qbitpay:3:ACCESS_LIMIT:%s:%s";

    @Resource
    private RedisService redisService;

    @Pointcut("@annotation(com.qbit.assets.common.annotation.AccessLimit)")
    public void match() {
    }

    @Around("match()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if (signature == null) {
            return joinPoint.proceed();
        }
        Method method = signature.getMethod();
        if (method == null) {
            return joinPoint.proceed();
        }

        AccessLimit access = method.getAnnotation(AccessLimit.class);
        if (access == null) {
            return joinPoint.proceed();
        }
        int seconds = access.seconds();
        int max = access.max();
        Class<?> clazz = method.getDeclaringClass();
        String className = clazz.getName().replace(clazz.getPackageName(), "").replaceAll("\\.", "");
        String key = String.format(ACCESS_LIMIT_KEY, className, method.getName());
        long increment = redisService.increment(key, seconds);
        if (increment > max) {
            throw new CustomException(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(), HttpStatus.TOO_MANY_REQUESTS);
        }

        return joinPoint.proceed();
    }
}
