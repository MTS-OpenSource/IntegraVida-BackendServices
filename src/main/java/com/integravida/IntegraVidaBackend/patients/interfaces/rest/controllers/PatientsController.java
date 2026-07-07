package com.integravida.IntegraVidaBackend.patients.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.ExternalProfileService;
import com.integravida.IntegraVidaBackend.patients.application.services.PatientCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.PatientQueryService;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreatePatientRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.PatientResource;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.UpdatePatientRequest;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Patients", description = "CRUD for patient records")
public class PatientsController {
    private final PatientCommandService commandService;
    private final PatientQueryService queryService;
    private final ExternalProfileService externalProfileService;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public PatientsController(PatientCommandService commandService,
                              PatientQueryService queryService,
                              ExternalProfileService externalProfileService,
                              JwtClaimsExtractor jwtClaimsExtractor) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.externalProfileService = externalProfileService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Operation(summary = "Create a patient", description = "Creates a patient linked to the authenticated profile.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Patient created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "5e4b5d6c-1c2d-4f0a-8b5c-7a6d9e0f1234",
                                      "profileId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                      "fullName": "Ana Pérez",
                                      "email": "ana.perez@integravida.com",
                                      "medicalRecordNumber": "MRN-000123",
                                      "notes": "Patient has type 2 diabetes and needs follow-up.",
                                      "active": true,
                                      "createdAt": "2026-06-13T08:30:01",
                                      "updatedAt": "2026-06-13T08:30:01"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePatientRequest request) {
        UUID profileId = UUID.fromString(jwtClaimsExtractor.extractProfileId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(profileId, request.medicalRecordNumber(), request.notes()),
                this::toResource,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get all patients", description = "Returns all patients in the system.")
    @ApiResponse(
            responseCode = "200",
            description = "Patients found",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PatientResource.class)),
                    examples = @ExampleObject(value = """
                            [
                              {
                                "id": "5e4b5d6c-1c2d-4f0a-8b5c-7a6d9e0f1234",
                                "profileId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                "fullName": "Ana Pérez",
                                "email": "ana.perez@integravida.com",
                                "medicalRecordNumber": "MRN-000123",
                                "notes": "Patient has type 2 diabetes and needs follow-up.",
                                "active": true,
                                "createdAt": "2026-06-13T08:30:01",
                                "updatedAt": "2026-06-13T08:30:01"
                              }
                            ]
                            """)
            )
    )
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<PatientResource> resources = queryService.getAll().stream().map(this::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get patient by id", description = "Returns a patient by its UUID.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResource.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "Patient UUID", example = "5e4b5d6c-1c2d-4f0a-8b5c-7a6d9e0f1234")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(id),
                this::toResource,
                HttpStatus.OK);
    }

    @Operation(summary = "Get patient by profile id", description = "Returns the patient linked to the authenticated profile.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResource.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/by-profile")
    public ResponseEntity<?> getByProfileId() {
        UUID profileId = UUID.fromString(jwtClaimsExtractor.extractProfileId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getByProfileId(profileId),
                this::toResource,
                HttpStatus.OK);
    }

    @Operation(summary = "Update patient notes", description = "Updates the notes for a patient.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResource.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "Patient UUID", example = "5e4b5d6c-1c2d-4f0a-8b5c-7a6d9e0f1234")
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePatientRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.updateNotes(id, request.notes()),
                this::toResource,
                HttpStatus.OK);
    }

    @Operation(summary = "Deactivate patient", description = "Marks a patient as inactive.")
    @ApiResponse(
            responseCode = "200",
            description = "Patient deactivated",
            content = @Content(schema = @Schema(implementation = PatientResource.class))
    )
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(
            @Parameter(description = "Patient UUID", example = "5e4b5d6c-1c2d-4f0a-8b5c-7a6d9e0f1234")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.deactivate(id),
                this::toResource,
                HttpStatus.OK);
    }

    @Operation(summary = "Reactivate patient", description = "Marks a patient as active again.")
    @ApiResponse(
            responseCode = "200",
            description = "Patient reactivated",
            content = @Content(schema = @Schema(implementation = PatientResource.class))
    )
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<?> reactivate(
            @Parameter(description = "Patient UUID", example = "5e4b5d6c-1c2d-4f0a-8b5c-7a6d9e0f1234")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.reactivate(id),
                this::toResource,
                HttpStatus.OK);
    }

    private PatientResource toResource(com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient patient) {
        var fullName = externalProfileService.getFullNameByProfileId(patient.getProfileId()).orElse(null);
        var email = externalProfileService.getEmailByProfileId(patient.getProfileId()).orElse(null);
        return PatientResource.fromDomain(patient, fullName, email);
    }
}
