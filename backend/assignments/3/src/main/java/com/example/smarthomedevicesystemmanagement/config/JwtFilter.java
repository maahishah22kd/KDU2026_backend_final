package com.example.smarthomedevicesystemmanagement.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                var claims = jwtUtil.extractClaims(token);

                String username = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);

                var authorities = roles.stream()
                        .map(role -> role.startsWith("ROLE_")
                                ? new SimpleGrantedAuthority(role)
                                : new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

                var authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("JWT authenticated user={} path={}", username, request.getRequestURI());

            } catch (JwtException | IllegalArgumentException ex) {
                SecurityContextHolder.clearContext();
                log.warn("JWT invalid/expired path={} reason={}", request.getRequestURI(), ex.getMessage());
            }
        } else {
            log.debug("JWT missing/ignored path={}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}
