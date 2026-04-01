package com.example.libraryservice.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.UUID;

/**
 * Aspect that logs audit events for methods annotated with {@link AuditAction}.
 *
 * <p>Runs after successful method execution and records:</p>
 * <ul>
 *   <li>username (from Spring Security {@link SecurityContextHolder})</li>
 *   <li>action (from {@link AuditAction#value()})</li>
 *   <li>bookId (resolved from method parameters)</li>
 * </ul>
 */

@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    @AfterReturning("@annotation(auditAction)")
    public void audit(JoinPoint jp,AuditAction auditAction) {
        MethodSignature sig = (MethodSignature) jp.getSignature();
        AuditAction ann = sig.getMethod().getAnnotation(AuditAction.class);

        String username = resolveUsername();
        UUID bookId = resolveBookId(sig, jp.getArgs());

        log.info("audit username={} action={} bookId={}", username, ann.value(), bookId);
    }

    private String resolveUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return "anonymous";
        return auth.getName();
    }

    // Finds a UUID parameter named "bookId" OR first UUID argument
    private UUID resolveBookId(MethodSignature sig, Object[] args) {
        Parameter[] params = sig.getMethod().getParameters();

        for (int i = 0; i < params.length; i++) {
            if ("bookId".equals(params[i].getName()) && args[i] instanceof UUID u) {
                return u;
            }
        }
        for (Object a : args) {
            if (a instanceof UUID u) return u;
        }
        return null;
    }
}
