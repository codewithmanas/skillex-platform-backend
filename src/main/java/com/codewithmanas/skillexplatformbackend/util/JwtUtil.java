package com.codewithmanas.skillexplatformbackend.util;

import com.codewithmanas.skillexplatformbackend.exception.InvalidTokenException;
import io.jsonwebtoken.*;
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

    public String generateResetPasswordToken(String email, String id) {
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

    /*  JWT Exceptions
    *   Exception	                 Meaning
        ExpiredJwtException	         Token is expired.
        UnsupportedJwtException	     Token has an unsupported format or algorithm.
        MalformedJwtException	     Token structure is invalid (e.g., wrong number of parts).
        SignatureException	         Token’s signature is invalid (tampered or wrong secret).
        SecurityException	         General cryptographic issue (e.g., invalid key).
        IllegalArgumentException	 Token is null or empty.
        *
        *         Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token) // throws SignatureException if signature is invalid
                .getBody()
                .getExpiration()
                .before(new Date());
    *
    * */

    // Check if token is valid or not
    public boolean isTokenInvalid(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                 SignatureException | SecurityException | IllegalArgumentException ex) {
            throw new InvalidTokenException("Invalid or expired JWT: " + ex.getMessage());

        }

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

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
