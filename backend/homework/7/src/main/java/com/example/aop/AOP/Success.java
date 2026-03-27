package com.example.aop.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class Success {

    @After("execution(* com.example.aop.Service.SmartLockService.unlock(..))")
    public void showSuccessStatus(){
        log.info("SUCCESS: User has entered the building");
    }
}
