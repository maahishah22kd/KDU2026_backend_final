package com.example.aop.AOP;
import com.example.aop.Exception.HardwareFailureException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SystemAlarm {

    @AfterThrowing(
            pointcut = "execution(* com.example.aop.Service.SmartLockService.unlock(..))",
            throwing = "ex"
    )
    public void alarm(Exception ex) {
        if (ex instanceof HardwareFailureException) {
            log.error("SYSTEM ALARM: {}", ex.getMessage());
        }
    }
}
