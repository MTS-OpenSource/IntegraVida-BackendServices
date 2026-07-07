package com.integravida.IntegraVidaBackend.iam.infrastructure.tokens;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JwtClaimsExtractor {

    @SuppressWarnings("unchecked")
    private Map<String, Object> getClaims() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            return (Map<String, Object>) authentication.getDetails();
        }
        return null;
    }

    public String extractUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    public Long extractUserId() {
        Map<String, Object> claims = getClaims();
        if (claims == null || claims.get("userId") == null) return null;
        Object value = claims.get("userId");
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.valueOf(value.toString());
    }

    public String extractRole() {
        Map<String, Object> claims = getClaims();
        return claims != null ? (String) claims.get("role") : null;
    }

    public String extractProfileId() {
        return getStringClaim("profileId");
    }

    public String extractPatientId() {
        return getStringClaim("patientId");
    }

    public String extractDoctorId() {
        return getStringClaim("doctorId");
    }

    private String getStringClaim(String key) {
        Map<String, Object> claims = getClaims();
        if (claims == null || claims.get(key) == null) {
            return null;
        }
        return claims.get(key).toString();
    }
}