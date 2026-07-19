package com.integravida.IntegraVidaBackend.patients.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.patients.application.services.MedicationIntakeCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.MedicationIntakeQueryService;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreateMedicationIntakeRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.MedicationIntakeResource;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medication-intakes")
@Tag(name = "Patients - Medication Intakes", description = "Register and view medication intakes")
public class MedicationIntakesController {
    private final MedicationIntakeCommandService commandService;
    private final MedicationIntakeQueryService queryService;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public MedicationIntakesController(MedicationIntakeCommandService commandService,
                                       MedicationIntakeQueryService queryService,
                                       JwtClaimsExtractor jwtClaimsExtractor) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Operation(summary = "Register medication intake", description = "Stores that a patient took a medication.")
    @ApiResponse(
            responseCode = "201",
            description = "Medication intake created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MedicationIntakeResource.class),
                    examples = @ExampleObject(value = """
                            {
                              "id": "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d",
                              "medicationId": "8d7c1e3a-5aa2-4f65-9b6f-6e4d8d2b4c55",
                              "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                              "takenAt": "2026-06-13T08:00:00",
                              "notes": "Taken after breakfast",
                              "createdAt": "2026-06-13T08:05:00"
                            }
                            """)
            )
    )
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateMedicationIntakeRequest request,
            @Parameter(description = "Patient UUID (required for ADMIN, optional for PATIENT)")
            @RequestParam(required = false) UUID patientId) {
        UUID effectivePatientId = patientId != null
                ? patientId
                : UUID.fromString(jwtClaimsExtractor.extractPatientId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.register(request.medicationId(), effectivePatientId, request.takenAt(), request.notes()),
                MedicationIntakeResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get all medication intakes", description = "Returns all medication intakes for the authenticated patient.")
    @ApiResponse(
            responseCode = "200",
            description = "Medication intakes found",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MedicationIntakeResource.class))
            )
    )
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAll(
            @Parameter(description = "Patient UUID (required for ADMIN/DOCTOR, optional for PATIENT)")
            @RequestParam(required = false) UUID patientId) {
        UUID effectivePatientId = patientId != null
                ? patientId
                : UUID.fromString(jwtClaimsExtractor.extractPatientId());
        List<MedicationIntakeResource> resources = queryService.getByPatientId(effectivePatientId)
                .stream().map(MedicationIntakeResource::fromDomain).toList();
        return ResponseEntity.ok(resources);
    }
}
