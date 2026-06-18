package com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.services;

import com.integravida.IntegraVidaBackend.iam.application.internal.outboundservices.tokens.TokenService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Placeholder implementation for TokenService.
 * Since no JWT library is currently configured in the pom.xml, this implements
 * basic dummy functionality to keep the application building and the architecture clean.
 *
 * Replace with a real JWT implementation (like jjwt or nimbus-jose-jwt) once added to the project dependencies.
 */
@Service
public class TokenServiceImpl implements TokenService {

    // Dummy implementation. Will just return a concatenated string to act as token.
    @Override
    public String generateToken(String username, String role, Long id) {
        // En una implementación real con io.jsonwebtoken:
        // return Jwts.builder()
        //         .setSubject(username)
        //         .claim("role", role)
        //         .claim("id", id)
        //         .setIssuedAt(new Date())
        //         .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        //         .signWith(SECRET_KEY)
        //         .compact();

        return username + "::" + role + "::" + id + "::" + UUID.randomUUID().toString();
    }

    @Override
    public String getUsernameFromToken(String token) {
        // En una implementación real:
        // return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
        if (token == null || !token.contains("::")) return null;
        return token.split("::")[0];
    }

    @Override
    public boolean validateToken(String token) {
        // En una implementación real: se usaría jwt parser para verificar si la firma aplica y la fecha es correcta
        return token != null && token.contains("::");
    }
}
