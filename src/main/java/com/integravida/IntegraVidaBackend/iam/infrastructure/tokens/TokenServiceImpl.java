package com.integravida.IntegraVidaBackend.iam.infrastructure.tokens;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class TokenServiceImpl implements TokenService {

    private final Key secretKey;
    private final long expirationTime;

    public TokenServiceImpl(@Value("${security.jwt.secret}") String jwtSecret,
                            @Value("${security.jwt.expiration-ms:86400000}") long expirationTime) {
        if (jwtSecret == null || jwtSecret.length() < 32) {
            throw new IllegalStateException("security.jwt.secret must be at least 32 characters long");
        }
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    @Override
    public String generateToken(String username, Long userId, String role, String profileId, String patientId, String doctorId) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", userId);
        claims.put("role", role);
        claims.put("profileId", profileId);

        if (patientId != null) claims.put("patientId", patientId);
        if (doctorId != null) claims.put("doctorId", doctorId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    @Override
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    @Override
    public String extractProfileId(String token) {
        return extractClaim(token, claims -> claims.get("profileId", String.class));
    }

    @Override
    public String extractPatientId(String token) {
        return extractClaim(token, claims -> claims.get("patientId", String.class));
    }

    @Override
    public String extractDoctorId(String token) {
        return extractClaim(token, claims -> claims.get("doctorId", String.class));
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return !extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
