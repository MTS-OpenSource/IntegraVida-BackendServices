package com.integravida.IntegraVidaBackend.iam.infrastructure.tokens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    private static final String USERNAME = "testuser";
    private static final Long USER_ID = 42L;
    private static final String ROLE = "PATIENT";
    private static final String PROFILE_ID = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa";
    private static final String PATIENT_ID = "dddddddd-dddd-dddd-dddd-dddddddddddd";
    private static final String DOCTOR_ID = "eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee";

    private String token;

    @BeforeEach
    void setUp() {
        token = tokenService.generateToken(USERNAME, USER_ID, ROLE, PROFILE_ID, PATIENT_ID, DOCTOR_ID);
    }

    @Test
    void generateToken_returnsNonNull() {
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_returnsCorrectValue() {
        assertEquals(USERNAME, tokenService.extractUsername(token));
    }

    @Test
    void extractUserId_returnsCorrectValue() {
        assertEquals(USER_ID, tokenService.extractUserId(token));
    }

    @Test
    void extractRole_returnsCorrectValue() {
        assertEquals(ROLE, tokenService.extractRole(token));
    }

    @Test
    void extractProfileId_returnsCorrectValue() {
        assertEquals(PROFILE_ID, tokenService.extractProfileId(token));
    }

    @Test
    void extractPatientId_returnsCorrectValue() {
        assertEquals(PATIENT_ID, tokenService.extractPatientId(token));
    }

    @Test
    void extractDoctorId_returnsCorrectValue() {
        assertEquals(DOCTOR_ID, tokenService.extractDoctorId(token));
    }

    @Test
    void validateToken_validToken_returnsTrue() {
        assertTrue(tokenService.validateToken(token));
    }

    @Test
    void validateToken_tokenWithNullDoctorId_returnsTrue() {
        String tokenWithoutDoctor = tokenService.generateToken(
                USERNAME, USER_ID, ROLE, PROFILE_ID, PATIENT_ID, null);
        assertTrue(tokenService.validateToken(tokenWithoutDoctor));
        assertNull(tokenService.extractDoctorId(tokenWithoutDoctor));
    }

    @Test
    void validateToken_tokenWithNullPatientId_returnsTrue() {
        String tokenWithoutPatient = tokenService.generateToken(
                USERNAME, USER_ID, ROLE, PROFILE_ID, null, DOCTOR_ID);
        assertTrue(tokenService.validateToken(tokenWithoutPatient));
        assertNull(tokenService.extractPatientId(tokenWithoutPatient));
    }

    @Test
    void validateToken_invalidToken_returnsFalse() {
        assertFalse(tokenService.validateToken("completely-invalid-token"));
    }
}
