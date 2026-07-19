package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.monitoring.application.services.GlucoseRangeCommandService;
import com.integravida.IntegraVidaBackend.monitoring.application.services.GlucoseRangeQueryService;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.GlucoseRangeRequest;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.GlucoseRangeResource;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Monitoring - Glucose Ranges", description = "Personalized glucose target configuration")
@RestController
@RequestMapping("/api/v1/glucose-ranges")
public class GlucoseRangeController {
    private final GlucoseRangeCommandService commandService;
    private final GlucoseRangeQueryService queryService;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public GlucoseRangeController(GlucoseRangeCommandService commandService, GlucoseRangeQueryService queryService, JwtClaimsExtractor jwtClaimsExtractor) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Operation(summary = "Get the active glucose range for the authenticated patient", description = "Returns the active glucose target range for the patient from the JWT.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Glucose range found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlucoseRangeResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "d12a2f9e-2d64-40b6-8f65-2b84f0f42010",
                                      "patientId": 1,
                                      "minimumValue": 70,
                                      "maximumValue": 180,
                                      "active": true,
                                      "createdAt": "2026-06-13T08:00:00",
                                      "updatedAt": "2026-06-13T08:00:00"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Glucose range not found")
    })
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getActive(
            @Parameter(description = "Patient UUID (required for ADMIN/DOCTOR, optional for PATIENT)")
            @RequestParam(required = false) UUID patientIdParam) {
        var patientId = patientIdParam != null
                ? PatientId.fromString(patientIdParam.toString())
                : PatientId.fromString(jwtClaimsExtractor.extractPatientId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getActiveByPatientId(patientId),
                GlucoseRangeResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(summary = "Create or update a glucose range", description = "Creates the active range for the authenticated patient or updates the existing one.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Glucose range saved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlucoseRangeResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "d12a2f9e-2d64-40b6-8f65-2b84f0f42010",
                                      "patientId": 1,
                                      "minimumValue": 75,
                                      "maximumValue": 170,
                                      "active": true,
                                      "createdAt": "2026-06-13T08:00:00",
                                      "updatedAt": "2026-06-13T10:00:00"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> upsert(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Glucose range payload",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlucoseRangeRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "minimumValue": 75,
                                      "maximumValue": 170
                                    }
                                    """)
                    )
            )
            @RequestBody GlucoseRangeRequest request,
            @Parameter(description = "Patient UUID (required for ADMIN/DOCTOR, optional for PATIENT)")
            @RequestParam(required = false) UUID patientIdParam) {
        var patientId = patientIdParam != null
                ? PatientId.fromString(patientIdParam.toString())
                : PatientId.fromString(jwtClaimsExtractor.extractPatientId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.upsert(patientId, GlucoseValue.of(request.minimumValue()), GlucoseValue.of(request.maximumValue())),
                GlucoseRangeResource::fromDomain,
                HttpStatus.OK);
    }
}
