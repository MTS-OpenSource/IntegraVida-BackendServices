package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.monitoring.application.services.ClinicalObservationCommandService;
import com.integravida.IntegraVidaBackend.monitoring.application.services.ClinicalObservationQueryService;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.ClinicalObservationResource;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.CreateClinicalObservationRequest;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.UpdateClinicalObservationRequest;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.resources.MessageResource;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@Tag(name = "Monitoring - Clinical Observations", description = "Clinical notes, diagnoses and structured observations")
@RestController
@RequestMapping("/api/v1/clinical-observations")
public class ClinicalObservationController {
    private final ClinicalObservationCommandService commandService;
    private final ClinicalObservationQueryService queryService;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public ClinicalObservationController(ClinicalObservationCommandService commandService, ClinicalObservationQueryService queryService, JwtClaimsExtractor jwtClaimsExtractor) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Operation(summary = "Create a clinical observation", description = "Stores a clinical note or structured observation for the authenticated patient.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Clinical observation created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClinicalObservationResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "5a3f2b11-47a6-4d8d-8f83-6f47d4f7e501",
                                      "patientId": 1,
                                      "category": "note",
                                      "title": "Post-meal review",
                                      "content": "Patient reported dizziness after lunch.",
                                      "observedAt": "2026-06-13T09:30:00",
                                      "createdAt": "2026-06-13T09:31:00",
                                      "updatedAt": "2026-06-13T09:31:00"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Clinical observation payload",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateClinicalObservationRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "category": "note",
                                      "title": "Post-meal review",
                                      "content": "Patient reported dizziness after lunch.",
                                      "observedAt": "2026-06-13T09:30:00"
                                    }
                                    """)
                    )
            )
            @RequestBody CreateClinicalObservationRequest request,
            @Parameter(description = "Patient UUID (required for ADMIN, optional for PATIENT)")
            @RequestParam(required = false) UUID patientIdParam) {
        var patientId = patientIdParam != null
                ? PatientId.fromString(patientIdParam.toString())
                : PatientId.fromString(jwtClaimsExtractor.extractPatientId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(patientId, request.category(), request.title(), request.content(), request.observedAt()),
                ClinicalObservationResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get a clinical observation by id", description = "Returns a single observation by identifier.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Clinical observation found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClinicalObservationResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "5a3f2b11-47a6-4d8d-8f83-6f47d4f7e501",
                                      "patientId": 1,
                                      "category": "note",
                                      "title": "Post-meal review",
                                      "content": "Patient reported dizziness after lunch.",
                                      "observedAt": "2026-06-13T09:30:00",
                                      "createdAt": "2026-06-13T09:31:00",
                                      "updatedAt": "2026-06-13T09:31:00"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Clinical observation not found")
    })
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "Clinical observation identifier", example = "5a3f2b11-47a6-4d8d-8f83-6f47d4f7e501")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(id),
                ClinicalObservationResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(summary = "List clinical observations for the authenticated patient", description = "Returns all observations for the patient from the JWT.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Clinical observations found",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClinicalObservationResource.class)),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": "5a3f2b11-47a6-4d8d-8f83-6f47d4f7e501",
                                        "patientId": 1,
                                        "category": "note",
                                        "title": "Post-meal review",
                                        "content": "Patient reported dizziness after lunch.",
                                        "observedAt": "2026-06-13T09:30:00",
                                        "createdAt": "2026-06-13T09:31:00",
                                        "updatedAt": "2026-06-13T09:31:00"
                                      }
                                    ]
                                    """)
                    )
            )
    })
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClinicalObservationResource>> findByPatientId(
            @Parameter(description = "Patient UUID (required for ADMIN/DOCTOR, optional for PATIENT)")
            @RequestParam(required = false) UUID patientIdParam) {
        var patientId = patientIdParam != null
                ? PatientId.fromString(patientIdParam.toString())
                : PatientId.fromString(jwtClaimsExtractor.extractPatientId());
        var observations = queryService.findByPatientId(patientId);
        return ResponseEntity.ok(observations.stream().map(ClinicalObservationResource::fromDomain).toList());
    }

    @Operation(summary = "Update a clinical observation", description = "Updates the content and metadata of an existing observation.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Clinical observation updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClinicalObservationResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "5a3f2b11-47a6-4d8d-8f83-6f47d4f7e501",
                                      "patientId": 1,
                                      "category": "report",
                                      "title": "Updated review",
                                      "content": "Updated clinical summary.",
                                      "observedAt": "2026-06-13T10:15:00",
                                      "createdAt": "2026-06-13T09:31:00",
                                      "updatedAt": "2026-06-13T10:20:00"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Clinical observation not found")
    })
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "Clinical observation identifier", example = "5a3f2b11-47a6-4d8d-8f83-6f47d4f7e501")
            @PathVariable UUID id,
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Clinical observation update payload",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateClinicalObservationRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "category": "report",
                                      "title": "Updated review",
                                      "content": "Updated clinical summary.",
                                      "observedAt": "2026-06-13T10:15:00"
                                    }
                                    """)
                    )
            )
            @RequestBody UpdateClinicalObservationRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.update(id, request.category(), request.title(), request.content(), request.observedAt()),
                ClinicalObservationResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(summary = "Delete a clinical observation", description = "Deletes an observation permanently.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Clinical observation deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "Clinical observation deleted: 5a3f2b11-47a6-4d8d-8f83-6f47d4f7e501"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Clinical observation not found")
    })
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "Clinical observation identifier", example = "5a3f2b11-47a6-4d8d-8f83-6f47d4f7e501")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.delete(id),
                deletedId -> new MessageResource("Clinical observation deleted: " + deletedId),
                HttpStatus.OK);
    }
}
