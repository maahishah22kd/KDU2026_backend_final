package com.example.aop.AOP;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class StopWatch {

    @Around("execution(* com.example.aop.Service.BatteryService.checkBattery(..))")
    public Object timeToCheckBattery(ProceedingJoinPoint joinPoint) throws Throwable{
        Long startTime= System.currentTimeMillis();
        Object result=joinPoint.proceed();
        Long endTime = System.currentTimeMillis();

        log.info("Battery check took {} ms", (endTime - startTime));
        return result;
    }
}
