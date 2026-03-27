package com.example.ticketbookingsystem;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Check if Authorization header is present and valid
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                // Extract claims from token
                var claims = jwtUtil.extractClaims(token);

                String username = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);

                // Convert roles to Spring Security authorities
                var authorities = roles.stream()
                        .map(role -> role.startsWith("ROLE_")
                                ? new SimpleGrantedAuthority(role)
                                : new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

                // Create authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Debug logs
                System.out.println("Authenticated user: " + username);
                System.out.println("Authorities: " + authorities);

            } catch (JwtException | IllegalArgumentException ex) {

                SecurityContextHolder.clearContext();

                // Let Spring Security handle 401 via AuthenticationEntryPoint
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
                return;
            }
        }
        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
