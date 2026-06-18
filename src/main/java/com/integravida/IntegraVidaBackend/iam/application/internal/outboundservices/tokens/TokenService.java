package com.integravida.IntegraVidaBackend.iam.application.internal.outboundservices.tokens;

public interface TokenService {
    String generateToken(String username, String role, Long id);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
}
