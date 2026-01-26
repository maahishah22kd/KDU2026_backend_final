package com.example.smarthomedevicesystemmanagement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.security.Key;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "super-secret-key-should-be-long-and-secure"; //too weak
    private static final long EXPIRATION_TIME = 1000 * 60 * 15; // 15 minutes

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Generate token
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Optional: extract username
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Optional: validate token
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
