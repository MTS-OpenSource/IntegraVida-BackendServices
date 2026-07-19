package com.integravida.IntegraVidaBackend.patients.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.patients.application.services.MedicationCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.MedicationQueryService;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.MedicationSchedule;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreateMedicationRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.MedicationResource;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medications")
@Tag(name = "Patients - Medications", description = "CRUD for medications")
public class MedicationsController {
    private final MedicationCommandService commandService;
    private final MedicationQueryService queryService;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public MedicationsController(MedicationCommandService commandService,
                                 MedicationQueryService queryService,
                                 JwtClaimsExtractor jwtClaimsExtractor) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Operation(summary = "Create medication", description = "Creates a medication linked to a treatment.")
    @ApiResponse(
            responseCode = "201",
            description = "Medication created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MedicationResource.class),
                    examples = @ExampleObject(value = """
                            {
                              "id": "8d7c1e3a-5aa2-4f65-9b6f-6e4d8d2b4c55",
                              "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                              "treatmentId": "2b2f7f3f-3d8a-4e6d-9c55-2f4f5b6c7d8e",
                              "name": "Metformin",
                              "dosage": "500 mg",
                              "daysOfWeek": ["MONDAY", "WEDNESDAY", "FRIDAY"],
                              "doseTimes": ["08:00", "20:00"],
                              "instructions": "Take with meals",
                              "active": true,
                              "discontinuedAt": null,
                              "createdAt": "2026-06-13T08:30:01",
                              "updatedAt": "2026-06-13T08:30:01"
                            }
                            """)
            )
    )
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateMedicationRequest request,
            @Parameter(description = "Patient UUID (required for ADMIN, optional for PATIENT)")
            @RequestParam(required = false) UUID patientId) {
        UUID effectivePatientId = patientId != null
                ? patientId
                : UUID.fromString(jwtClaimsExtractor.extractPatientId());
        var schedule = MedicationSchedule.of(request.daysOfWeek(), request.doseTimes(), request.instructions());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(effectivePatientId, request.treatmentId(), request.name(), request.dosage(), schedule),
                MedicationResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get medications", description = "Returns medications for the authenticated patient, optionally filtered by treatment.")
    @ApiResponse(
            responseCode = "200",
            description = "Medications found",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MedicationResource.class)),
                    examples = @ExampleObject(value = """
                            [
                              {
                                "id": "8d7c1e3a-5aa2-4f65-9b6f-6e4d8d2b4c55",
                                "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                "treatmentId": "2b2f7f3f-3d8a-4e6d-9c55-2f4f5b6c7d8e",
                                "name": "Metformin",
                                "dosage": "500 mg",
                                "daysOfWeek": ["MONDAY", "WEDNESDAY", "FRIDAY"],
                                "doseTimes": ["08:00", "20:00"],
                                "instructions": "Take with meals",
                                "active": true,
                                "discontinuedAt": null,
                                "createdAt": "2026-06-13T08:30:01",
                                "updatedAt": "2026-06-13T08:30:01"
                              }
                            ]
                            """)
            )
    )
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAll(
            @Parameter(description = "Optional treatment UUID", example = "2b2f7f3f-3d8a-4e6d-9c55-2f4f5b6c7d8e")
            @RequestParam(required = false) UUID treatmentId,
            @Parameter(description = "Patient UUID (required for ADMIN/DOCTOR, optional for PATIENT)")
            @RequestParam(required = false) UUID patientId) {
        UUID effectivePatientId = patientId != null
                ? patientId
                : UUID.fromString(jwtClaimsExtractor.extractPatientId());
        if (treatmentId != null) {
            List<MedicationResource> resources = queryService.getByTreatmentId(treatmentId).stream()
                    .filter(m -> m.getPatientId().value().equals(effectivePatientId))
                    .map(MedicationResource::fromDomain).toList();
            return ResponseEntity.ok(resources);
        }
        List<MedicationResource> resources = queryService.getByPatientId(effectivePatientId).stream().map(MedicationResource::fromDomain).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get medication by id", description = "Returns a medication by UUID.")
    @ApiResponse(
            responseCode = "200",
            description = "Medication found",
            content = @Content(schema = @Schema(implementation = MedicationResource.class))
    )
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "Medication UUID", example = "8d7c1e3a-5aa2-4f65-9b6f-6e4d8d2b4c55")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(id),
                MedicationResource::fromDomain,
                HttpStatus.OK);
    }
}
