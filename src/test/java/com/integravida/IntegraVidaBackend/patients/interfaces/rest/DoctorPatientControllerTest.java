package com.integravida.IntegraVidaBackend.patients.interfaces.rest;

import com.integravida.IntegraVidaBackend.security.TestJwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DoctorPatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String PATIENT_ID = "dddddddd-dddd-dddd-dddd-dddddddddddd";
    private static final String OTHER_PATIENT_ID = "11111111-1111-1111-1111-111111111111";
    private static final String DOCTOR_ID = "eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee";

    @Test
    void doctorListMyPatients_returns200() throws Exception {
        String token = TestJwtHelper.doctorToken(DOCTOR_ID);
        mockMvc.perform(get("/api/v1/doctors/me/patients")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void doctorGetAssignedPatientSummary_returns200() throws Exception {
        String token = TestJwtHelper.doctorToken(DOCTOR_ID);
        mockMvc.perform(get("/api/v1/doctors/me/patients/" + PATIENT_ID + "/summary")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(PATIENT_ID));
    }

    @Test
    void doctorGetUnassignedPatientSummary_returns403() throws Exception {
        String token = TestJwtHelper.doctorToken(DOCTOR_ID);
        mockMvc.perform(get("/api/v1/doctors/me/patients/" + OTHER_PATIENT_ID + "/summary")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void doctorGetAssignedPatientTreatments_returns200() throws Exception {
        String token = TestJwtHelper.doctorToken(DOCTOR_ID);
        mockMvc.perform(get("/api/v1/doctors/me/patients/" + PATIENT_ID + "/treatments")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void doctorCreateTreatmentForAssignedPatient_returns201() throws Exception {
        String token = TestJwtHelper.doctorToken(DOCTOR_ID);
        String body = """
                {
                    "name": "Insulin Therapy",
                    "description": "Daily insulin management",
                    "startDate": "2026-01-01",
                    "endDate": "2026-12-31"
                }
                """;
        mockMvc.perform(post("/api/v1/doctors/me/patients/" + PATIENT_ID + "/treatments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void doctorCreateTreatmentForUnassignedPatient_returns403() throws Exception {
        String token = TestJwtHelper.doctorToken(DOCTOR_ID);
        String body = """
                {
                    "name": "Insulin Therapy",
                    "description": "Daily insulin management",
                    "startDate": "2026-01-01",
                    "endDate": "2026-12-31"
                }
                """;
        mockMvc.perform(post("/api/v1/doctors/me/patients/" + OTHER_PATIENT_ID + "/treatments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthenticatedAccessToDoctorEndpoint_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/doctors/me/patients"))
                .andExpect(status().isForbidden());
    }

    @Test
    void patientAccessDoctorEndpoint_returns403() throws Exception {
        String token = TestJwtHelper.patientToken(PATIENT_ID);
        mockMvc.perform(get("/api/v1/doctors/me/patients")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
