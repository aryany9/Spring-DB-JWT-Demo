package com.example.demo.securities;

import com.example.demo.models.dto.CustomerCredentials;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static javax.crypto.Cipher.SECRET_KEY;

@Slf4j
@Component
public class JwtUtil {
    private final String SECRET = "your-secret-key-should-be-long-and-secure";

    private Key getSignInKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        log.debug("Generating Token");
        return buildToken(userDetails, 1000 * 60 * 15); // 15 minutes
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, 1000 * 60 * 60 * 24 * 7); // 7 days
    }

    private String buildToken(UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isRefreshTokenValid(String token, CustomerCredentials credentials) {
        final String username = extractUsername(token);
        return username.equals(credentials.getUsername()) && !isTokenExpired(token);
    }

    public Optional<String> extractUser(String token) {
        try {
            return Optional.ofNullable(extractUsername(token));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
