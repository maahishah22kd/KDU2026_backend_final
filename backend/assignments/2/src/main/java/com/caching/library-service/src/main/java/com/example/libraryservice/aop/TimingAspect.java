package com.example.libraryservice.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimingAspect {

    private static final Logger log = LoggerFactory.getLogger(TimingAspect.class);

    // Apply to all public methods inside service package
    @Around("execution(public * com.example.libraryservice.service..*(..))")
    public Object timeServiceMethods(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long durationMs = System.currentTimeMillis() - start;
            log.info("timing method={} durationMs={}", pjp.getSignature().toShortString(), durationMs);
        }
    }
}
