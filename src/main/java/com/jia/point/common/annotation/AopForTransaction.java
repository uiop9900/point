package com.jia.point.common.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 하나의 트랜잭션(하나의 잡)이 끝난 이후에 락을 반환해야 한다.
 * 상태변경까지 끝마쳐야 다음 트랜잭션에서 반영된 값에서 조회해서 확인하기 때문.
 */
@Component
public class AopForTransaction {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
