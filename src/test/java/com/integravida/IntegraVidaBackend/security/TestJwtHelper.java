package com.integravida.IntegraVidaBackend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestJwtHelper {

    private static final String SECRET = "test-secret-key-for-unit-tests-that-is-at-least-32-chars";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(String username, Long userId, String role,
                                       String profileId, String patientId, String doctorId) {
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
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(KEY)
                .compact();
    }

    public static String patientToken(String patientId) {
        return generateToken("testpatient", 1L, "PATIENT",
                "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", patientId, null);
    }

    public static String doctorToken(String doctorId) {
        return generateToken("testdoctor", 2L, "DOCTOR",
                "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb", null, doctorId);
    }

    public static String adminToken() {
        return generateToken("testadmin", 3L, "ADMIN",
                "cccccccc-cccc-cccc-cccc-cccccccccccc", null, null);
    }
}
