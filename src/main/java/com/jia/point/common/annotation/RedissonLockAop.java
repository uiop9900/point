package com.jia.point.common.annotation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @RedissonLock 선언 시 수행되는 AOP<br>
 * 정상적으로 lock이 잡히게 되면 아래와 같이 로그가 뜬다. <br>
 * [lock을 시도합니다.]<br>
 * [proceed를 호출합니다.]<br>
 * [unLock을 시도합니다.]<br>
 *
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAop {
    private static final String REDISSON_LOCK_PREFIX = "REDISSON:";

    private final RedissonClient redissonClient;

    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.jia.point.common.annotation.RedissonLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);

        String key = REDISSON_LOCK_PREFIX + redissonLock.key(); // REDISSON:{key}
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean available = rLock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeUnit());
            log.info("[lock을 시도합니다.]");
            if (!available) {
                return false;
            }

//            return joinPoint.proceed();
            log.info("[proceed를 호출합니다.]");
            return aopForTransaction.proceed(joinPoint);  // 따로 정의된 proceed를 호출함 -> 매번 new Transactional
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                rLock.unlock();
                log.info("[unLock을 시도합니다.]");
            } catch (IllegalMonitorStateException e) {
                log.info("[Redisson Lock Already UnLock] method = {}, key = {}",
                         method.getName(), key);
            }
        }
    }
}
