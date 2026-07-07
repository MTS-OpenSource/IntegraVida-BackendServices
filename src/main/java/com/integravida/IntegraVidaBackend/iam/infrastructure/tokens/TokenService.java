package com.integravida.IntegraVidaBackend.iam.infrastructure.tokens;

import java.util.Map;

public interface TokenService {
    String generateToken(String username, Long userId, String role, Long profileId, Long patientId, Long doctorId);

    String extractUsername(String token);
    Long extractUserId(String token);
    String extractRole(String token);
    Long extractProfileId(String token);
    Long extractPatientId(String token);
    Long extractDoctorId(String token);
    boolean validateToken(String token);
}