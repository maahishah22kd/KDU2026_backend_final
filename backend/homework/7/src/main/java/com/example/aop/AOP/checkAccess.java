package com.example.aop.AOP;
import com.example.aop.Exception.HardwareFailureException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.example.aop.Annotations.RequiresRole;

@Aspect
@Component
@Slf4j
public class checkAccess {

    @Around("@annotation(requiresRole)")
    public Object checkRole(
            ProceedingJoinPoint jp,
            RequiresRole requiresRole
    ) throws Throwable {

        String user = (String) jp.getArgs()[0];

        if (user == null || user.isBlank()) {
            throw new HardwareFailureException();
        }

        if (requiresRole.value().equals(user)) {
            return jp.proceed();
        }

        log.warn("SECURITY ALERT: Unauthorized access blocked!");
        return null;
    }
}
