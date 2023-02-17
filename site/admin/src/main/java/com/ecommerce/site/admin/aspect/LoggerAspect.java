package com.ecommerce.site.admin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Nguyen Thanh Phuong
 */
@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Around("execution(* com.ecommerce.site.admin.*.*(..))")
    public Object log(@NotNull ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(joinPoint.getSignature().toString() + " method execution start");
        Instant startTime = Instant.now();
        Object result = joinPoint.proceed();
        Instant endTime = Instant.now();
        long timeElapsed = Duration.between(startTime, endTime).toMillis();
        log.info("Time took to execute " + joinPoint.getSignature().toString() + " method is " + timeElapsed);
        log.info(joinPoint.getSignature().toString() + " method execution end");

        return result;
    }

    @AfterThrowing(value = "execution(* com.ecommerce.site.admin.*.*(..))", throwing = "e")
    public void logException(@NotNull JoinPoint joinPoint, @NotNull Exception e) {
        log.error(joinPoint.getSignature().toString() + " An exception happened due to " + e.getMessage());
    }

}
