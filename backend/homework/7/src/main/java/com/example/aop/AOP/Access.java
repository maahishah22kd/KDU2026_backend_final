package com.example.aop.AOP;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class Access {
    @Before("execution(* com.example.aop.Service.SmartLockService.unlock(..))")
    public void logAccessAttempt(){
        log.info("ACCESS ATTEMPT: User is approaching the door");
    }
}
