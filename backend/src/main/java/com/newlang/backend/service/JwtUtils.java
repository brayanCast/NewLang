package com.newlang.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final SecretKey signingKey;
    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24;       // 24 hours
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7;  // 7 days

    public JwtUtils(@Value("${jwt.secret.key}") String secretKey) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return generateToken(claims, userDetails, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return generateToken(claims, userDetails, REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails, long expirationMillis) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(signingKey)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
