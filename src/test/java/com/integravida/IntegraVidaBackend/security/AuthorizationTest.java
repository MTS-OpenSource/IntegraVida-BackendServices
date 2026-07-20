package com.integravida.IntegraVidaBackend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String PATIENT_ID = "dddddddd-dddd-dddd-dddd-dddddddddddd";
    private static final String OTHER_PATIENT_ID = "11111111-1111-1111-1111-111111111111";
    private static final String DOCTOR_ID = "eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee";

    @Test
    void unauthenticatedAccess_toProtectedEndpoint_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/patients"))
                .andExpect(status().isForbidden());
    }

    @Test
    void patient_accessOwnData_returns200() throws Exception {
        String token = TestJwtHelper.patientToken(PATIENT_ID);
        mockMvc.perform(get("/api/v1/patients/" + PATIENT_ID)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void patient_accessOtherPatientData_returns403() throws Exception {
        String token = TestJwtHelper.patientToken(PATIENT_ID);
        mockMvc.perform(get("/api/v1/patients/" + OTHER_PATIENT_ID)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void patient_listAllPatients_returns403() throws Exception {
        String token = TestJwtHelper.patientToken(PATIENT_ID);
        mockMvc.perform(get("/api/v1/patients")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void doctor_accessAssignedPatient_returns200() throws Exception {
        String token = TestJwtHelper.doctorToken(DOCTOR_ID);
        mockMvc.perform(get("/api/v1/patients/" + PATIENT_ID)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void doctor_accessUnassignedPatient_returns403() throws Exception {
        String token = TestJwtHelper.doctorToken(DOCTOR_ID);
        mockMvc.perform(get("/api/v1/patients/" + OTHER_PATIENT_ID)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void doctor_listOwnPatients_returns200() throws Exception {
        String token = TestJwtHelper.doctorToken(DOCTOR_ID);
        mockMvc.perform(get("/api/v1/doctors/me/patients")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void admin_accessAnyPatient_returns200() throws Exception {
        String token = TestJwtHelper.adminToken();
        mockMvc.perform(get("/api/v1/patients/" + PATIENT_ID)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void admin_listAllPatients_returns200() throws Exception {
        String token = TestJwtHelper.adminToken();
        mockMvc.perform(get("/api/v1/patients")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
