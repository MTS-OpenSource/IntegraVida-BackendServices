package com.integravida.IntegraVidaBackend.iam.infrastructure.tokens;

import java.util.Map;

public interface TokenService {
    String generateToken(String username, Long userId, String role, String profileId, String patientId, String doctorId);

    String extractUsername(String token);
    Long extractUserId(String token);
    String extractRole(String token);
    String extractProfileId(String token);
    String extractPatientId(String token);
    String extractDoctorId(String token);
    boolean validateToken(String token);
}