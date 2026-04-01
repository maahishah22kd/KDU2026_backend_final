package com.example.libraryservice.aop;

import java.lang.annotation.*;

/**
 * Marks a service method as auditable.
 *
 * <p>The {@link AuditAspect} intercepts methods annotated with {@code @AuditAction} and logs
 * the authenticated username, action value, and related bookId.</p>
 *
 * <p>Example: {@code @AuditAction("borrow")}.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditAction {
    String value(); // e.g. "create", "catalog", "borrow", "return"
}
