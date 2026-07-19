package com.integravida.IntegraVidaBackend.patients.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.patients.application.services.TreatmentCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.TreatmentQueryService;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreateTreatmentRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.TreatmentResource;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.UpdateTreatmentRequest;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/treatments")
@Tag(name = "Patients - Treatments", description = "CRUD for treatments")
public class TreatmentsController {
    private final TreatmentCommandService commandService;
    private final TreatmentQueryService queryService;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public TreatmentsController(TreatmentCommandService commandService,
                                TreatmentQueryService queryService,
                                JwtClaimsExtractor jwtClaimsExtractor) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Operation(summary = "Create a treatment", description = "Creates a treatment for the authenticated patient.")
    @ApiResponse(
            responseCode = "201",
            description = "Treatment created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TreatmentResource.class),
                    examples = @ExampleObject(value = """
                            {
                              "id": "7a2d2d4e-4df0-4d42-a6ef-5b1d1f5a2c33",
                              "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                              "name": "Metformin plan",
                              "description": "Daily glucose control plan",
                              "startDate": "2026-06-01",
                              "endDate": "2026-12-01",
                              "status": "ACTIVE",
                              "createdAt": "2026-06-13T08:30:01",
                              "updatedAt": "2026-06-13T08:30:01"
                            }
                            """)
            )
    )
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateTreatmentRequest request,
            @Parameter(description = "Patient UUID (required for ADMIN, optional for PATIENT)")
            @RequestParam(required = false) UUID patientId) {
        UUID effectivePatientId = patientId != null
                ? patientId
                : UUID.fromString(jwtClaimsExtractor.extractPatientId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(effectivePatientId, request.name(), request.description(), request.startDate(), request.endDate()),
                TreatmentResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get treatments", description = "Returns treatments for the authenticated patient.")
    @ApiResponse(
            responseCode = "200",
            description = "Treatments found",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TreatmentResource.class)),
                    examples = @ExampleObject(value = """
                            [
                              {
                                "id": "7a2d2d4e-4df0-4d42-a6ef-5b1d1f5a2c33",
                                "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                "name": "Metformin plan",
                                "description": "Daily glucose control plan",
                                "startDate": "2026-06-01",
                                "endDate": "2026-12-01",
                                "status": "ACTIVE",
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
            @Parameter(description = "Patient UUID (required for ADMIN/DOCTOR, optional for PATIENT)")
            @RequestParam(required = false) UUID patientId) {
        UUID effectivePatientId = patientId != null
                ? patientId
                : UUID.fromString(jwtClaimsExtractor.extractPatientId());
        List<TreatmentResource> resources = queryService.getByPatientId(effectivePatientId).stream().map(TreatmentResource::fromDomain).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get treatment by id", description = "Returns a treatment by UUID.")
    @ApiResponse(
            responseCode = "200",
            description = "Treatment found",
            content = @Content(schema = @Schema(implementation = TreatmentResource.class))
    )
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "Treatment UUID", example = "7a2d2d4e-4df0-4d42-a6ef-5b1d1f5a2c33")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(id),
                TreatmentResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(summary = "Get active treatment", description = "Returns the active treatment for the authenticated patient.")
    @ApiResponse(
            responseCode = "200",
            description = "Active treatment found",
            content = @Content(schema = @Schema(implementation = TreatmentResource.class))
    )
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @GetMapping("/active")
    public ResponseEntity<?> getActive(
            @Parameter(description = "Patient UUID (required for ADMIN, optional for PATIENT)")
            @RequestParam(required = false) UUID patientId) {
        UUID effectivePatientId = patientId != null
                ? patientId
                : UUID.fromString(jwtClaimsExtractor.extractPatientId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getActiveByPatientId(effectivePatientId),
                TreatmentResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(summary = "Update treatment", description = "Updates an existing treatment by UUID.")
    @ApiResponse(
            responseCode = "200",
            description = "Treatment updated",
            content = @Content(schema = @Schema(implementation = TreatmentResource.class))
    )
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "Treatment UUID", example = "7a2d2d4e-4df0-4d42-a6ef-5b1d1f5a2c33")
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTreatmentRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.update(id, request.name(), request.description(), request.startDate(), request.endDate(), request.status()),
                TreatmentResource::fromDomain,
                HttpStatus.OK);
    }
}
