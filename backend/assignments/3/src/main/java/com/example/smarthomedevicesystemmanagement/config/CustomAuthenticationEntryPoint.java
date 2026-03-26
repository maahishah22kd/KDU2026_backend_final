package com.example.smarthomedevicesystemmanagement.config;

import com.example.smarthomedevicesystemmanagement.exception.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "Authentication is required to access this resource",
                request.getRequestURI()
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(toJson(body));
    }

    private String toJson(ApiError e) {
        return """
            {
              "timestamp": "%s",
              "status": %d,
              "error": "%s",
              "message": "%s",
              "path": "%s"
            }
            """.formatted(
                e.timestamp(), e.status(),
                escape(e.error()), escape(e.message()), escape(e.path())
        );
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
