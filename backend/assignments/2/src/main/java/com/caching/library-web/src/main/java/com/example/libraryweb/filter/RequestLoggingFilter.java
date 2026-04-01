package com.example.libraryweb.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Servlet filter that logs request start and request completion.
 *
 * <p>Logs include method, path, status, latency, and correlationId (read from MDC). This provides a
 * consistent request lifecycle log for successful responses and errors (401/403/4xx/5xx).</p>
 *
 * <p>Typically ordered late in the filter chain so latency includes security + controller processing.</p>
 */

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();

        String method = request.getMethod();
        String path = request.getRequestURI();
        String correlationId = MDC.get(CorrelationIdFilter.MDC_KEY);

        log.info("request.start method={} path={} correlationId={}", method, path, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long latencyMs = System.currentTimeMillis() - start;
            int status = response.getStatus();

            log.info("request.end method={} path={} status={} latencyMs={} correlationId={}",
                    method, path, status, latencyMs, correlationId);
        }
    }
}
