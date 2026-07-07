package com.integravida.IntegraVidaBackend.patients.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.patients.application.services.DoctorCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.DoctorQueryService;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.AssignPatientRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreateDoctorRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.DoctorResource;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.PatientAssignmentResource;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors")
@Tag(name = "Patients - Doctors", description = "CRUD for doctor records and patient assignment")
public class DoctorController {
    private final DoctorCommandService commandService;
    private final DoctorQueryService queryService;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public DoctorController(DoctorCommandService commandService,
                            DoctorQueryService queryService,
                            JwtClaimsExtractor jwtClaimsExtractor) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Operation(summary = "Create a doctor", description = "Creates a doctor linked to the authenticated profile.")
    @ApiResponse(
            responseCode = "201",
            description = "Doctor created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DoctorResource.class),
                    examples = @ExampleObject(value = """
                            {
                              "id": "66666666-6666-6666-6666-666666666666",
                              "profileId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                              "doctorRecordNumber": "DOC-000123",
                              "notes": "Cardiologist specializing in diabetes care.",
                              "active": true,
                              "createdAt": "2026-06-13T08:30:01",
                              "updatedAt": "2026-06-13T08:30:01"
                            }
                            """)
            )
    )
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateDoctorRequest request) {
        UUID profileId = UUID.fromString(jwtClaimsExtractor.extractProfileId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(profileId, request.doctorRecordNumber(), request.notes()),
                DoctorResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Assign patient to doctor", description = "Assigns a patient to the authenticated doctor.")
    @ApiResponse(
            responseCode = "201",
            description = "Patient assigned to doctor",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PatientAssignmentResource.class)
            )
    )
    @PostMapping("/assign-patient")
    public ResponseEntity<?> assignPatient(@Valid @RequestBody AssignPatientRequest request) {
        UUID doctorId = UUID.fromString(jwtClaimsExtractor.extractDoctorId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.assignPatient(request.patientId(), doctorId),
                PatientAssignmentResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get patients by doctor", description = "Returns all patients assigned to the authenticated doctor.")
    @ApiResponse(
            responseCode = "200",
            description = "Patients found",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PatientAssignmentResource.class))
            )
    )
    @GetMapping("/patients")
    public ResponseEntity<?> getPatients() {
        UUID doctorId = UUID.fromString(jwtClaimsExtractor.extractDoctorId());
        List<PatientAssignmentResource> resources = queryService.getPatientsByDoctorId(doctorId)
                .stream().map(PatientAssignmentResource::fromDomain).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get doctor by patient", description = "Returns the doctor assigned to a specific patient.")
    @ApiResponse(
            responseCode = "200",
            description = "Doctor assignment found",
            content = @Content(schema = @Schema(implementation = PatientAssignmentResource.class))
    )
    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<?> getDoctorByPatientId(@PathVariable UUID patientId) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getDoctorByPatientId(patientId),
                PatientAssignmentResource::fromDomain,
                HttpStatus.OK);
    }
}
