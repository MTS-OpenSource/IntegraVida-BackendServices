package com.integravida.IntegraVidaBackend.iam.interfaces.rest;

import com.integravida.IntegraVidaBackend.security.TestJwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void adminListUsers_returns200() throws Exception {
        String token = TestJwtHelper.adminToken();
        mockMvc.perform(get("/api/v1/admin/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void nonAdminListUsers_returns403() throws Exception {
        String token = TestJwtHelper.patientToken("dddddddd-dddd-dddd-dddd-dddddddddddd");
        mockMvc.perform(get("/api/v1/admin/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminListDoctors_returns200() throws Exception {
        String token = TestJwtHelper.adminToken();
        mockMvc.perform(get("/api/v1/admin/doctors")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void adminDashboard_returns200() throws Exception {
        String token = TestJwtHelper.adminToken();
        mockMvc.perform(get("/api/v1/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").isNumber())
                .andExpect(jsonPath("$.totalPatients").isNumber())
                .andExpect(jsonPath("$.totalDoctors").isNumber());
    }

    @Test
    void adminCreateDoctor_returns201() throws Exception {
        String token = TestJwtHelper.adminToken();
        String body = """
                {
                    "username": "newdoctor",
                    "password": "password123",
                    "email": "newdoctor@test.com",
                    "firstName": "New",
                    "lastName": "Doctor",
                    "phoneNumber": "555-9999",
                    "dateOfBirth": "1990-01-01",
                    "doctorRecordNumber": "DOC-NEW-001",
                    "doctorNotes": "New test doctor"
                }
                """;
        mockMvc.perform(post("/api/v1/admin/doctors")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorRecordNumber").value("DOC-NEW-001"));
    }

    @Test
    void adminCreateAdmin_returns201() throws Exception {
        String token = TestJwtHelper.adminToken();
        String body = """
                {
                    "username": "newadmin",
                    "password": "password123",
                    "email": "newadmin@test.com",
                    "firstName": "New",
                    "lastName": "Admin",
                    "phoneNumber": "555-8888",
                    "dateOfBirth": "1985-05-05"
                }
                """;
        mockMvc.perform(post("/api/v1/admin/admins")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void adminCreateAndDeleteAssignment_lifecycle() throws Exception {
        String token = TestJwtHelper.adminToken();

        String createDoctorBody = """
                {
                    "username": "assigndoc",
                    "password": "password123",
                    "email": "assigndoc@test.com",
                    "firstName": "Assign",
                    "lastName": "Doctor",
                    "phoneNumber": "555-7777",
                    "dateOfBirth": "1990-01-01",
                    "doctorRecordNumber": "DOC-ASSIGN-001",
                    "doctorNotes": "Test assignment doctor"
                }
                """;
        var createResult = mockMvc.perform(post("/api/v1/admin/doctors")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDoctorBody))
                .andExpect(status().isCreated())
                .andReturn();
        String newDoctorId = com.jayway.jsonpath.JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

        String assignBody = """
                {
                    "patientId": "11111111-1111-1111-1111-111111111111",
                    "doctorId": "%s"
                }
                """.formatted(newDoctorId);
        var assignResult = mockMvc.perform(post("/api/v1/admin/assignments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assignBody))
                .andExpect(status().isCreated())
                .andReturn();
        String assignmentId = com.jayway.jsonpath.JsonPath.read(assignResult.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(delete("/api/v1/admin/assignments/" + assignmentId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void nonAdminAccessAdminEndpoint_returns403() throws Exception {
        String token = TestJwtHelper.patientToken("dddddddd-dddd-dddd-dddd-dddddddddddd");
        mockMvc.perform(get("/api/v1/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
