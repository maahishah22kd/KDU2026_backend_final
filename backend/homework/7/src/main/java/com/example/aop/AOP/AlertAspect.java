package com.example.aop.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AlertAspect {

    @AfterThrowing(
            pointcut = "execution(* com.example.aop.Service.SmartLockService.*(..))",
            throwing = "ex"
    )
    public void triggerAlarm(Exception ex) {
        log.error(
                "ALARM TRIGGERED: System error detected: {}",
                ex.getMessage()
        );

        callEmergencyService();
    }

    private void callEmergencyService() {
        log.error("Emergency service has been notified!");
    }
}
