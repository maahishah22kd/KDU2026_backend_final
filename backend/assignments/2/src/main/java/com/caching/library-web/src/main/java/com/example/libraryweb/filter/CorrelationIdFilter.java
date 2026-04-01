package com.example.libraryweb.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter that manages request correlation identifiers.
 *
 * <p>Behavior:</p>
 * <ul>
 *   <li>Reads {@code X-Correlation-Id} request header if present.</li>
 *   <li>If missing or blank, generates a new UUID.</li>
 *   <li>Stores the correlationId in SLF4J MDC so all logs during the request include it.</li>
 *   <li>Echoes the correlationId back in the response header.</li>
 * </ul>
 *
 * <p>Important: MDC is cleared at the end of the request to prevent leakage across requests when
 * servlet threads are reused.</p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Correlation-Id";
    public static final String MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId = request.getHeader(HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(MDC_KEY, correlationId);

        // also make it available to your GlobalExceptionHandler base() method
        request.setAttribute(MDC_KEY, correlationId);

        // always return the header back to client
        response.setHeader(HEADER, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY); // critical to avoid leakage across requests/threads
        }
    }
}
