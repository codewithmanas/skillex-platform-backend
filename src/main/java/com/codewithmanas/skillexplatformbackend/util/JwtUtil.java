package com.codewithmanas.skillexplatformbackend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateVerificationToken(String email, String id) {
        return Jwts.builder()
                .subject(id)
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(900))) // 15 minutes
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String id, String role) {
        return Jwts.builder()
                .subject(id)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(3600 * 24 * 7))) // 7 days
                .signWith(secretKey)
                .compact();
    }

    public String generateAccessToken(String email, String id, String role) {
        return Jwts.builder()
                .subject(id)
                .claims(Map.of(
                        "email", email,
                        "role", role
                ))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(3600 * 24))) // 24 hours
                .signWith(secretKey)
                .compact();
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // Extract Email
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class); // get from claims, not subject
    }

    // Extract User Id
    public String extractUserId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // subject now holds the user ID
    }

    // Extract User Role
    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }


    public String generateResetPasswordToken(String email, String id) {
        return Jwts.builder()
                .subject(id)
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(900))) // 15 minutes
                .signWith(secretKey)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
