package com.example.libraryweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REST API: disable CSRF for stateless requests
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Swagger/OpenAPI should be public (optional but practical)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Authorization rules (from your table)
                        .requestMatchers(HttpMethod.POST, "/books").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.PATCH, "/books/*/catalog").hasRole("LIBRARIAN")

                        .requestMatchers(HttpMethod.GET, "/analytics/audit").hasRole("LIBRARIAN")

                        .requestMatchers(HttpMethod.GET, "/books").hasAnyRole("LIBRARIAN", "MEMBER")

                        .requestMatchers(HttpMethod.POST, "/loans/*/borrow").hasAnyRole("MEMBER")

                        // MEMBERS only (as stated)
                        .requestMatchers(HttpMethod.POST, "/loans/*/return").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.GET, "/analytics/audit").hasRole("MEMBER")

                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
