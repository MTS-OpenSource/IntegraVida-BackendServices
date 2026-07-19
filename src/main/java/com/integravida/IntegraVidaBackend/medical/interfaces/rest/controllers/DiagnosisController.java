package com.integravida.IntegraVidaBackend.medical.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.medical.application.services.DiagnosisCommandService;
import com.integravida.IntegraVidaBackend.medical.application.services.DiagnosisQueryService;

import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.CreateDiagnosisRequest;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.DiagnosisResource;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Medical - Diagnoses", description = "Medical diagnosis management endpoints")
@RestController
@RequestMapping("/api/v1/diagnoses")
public class DiagnosisController {
    private final DiagnosisCommandService commandService;
    private final DiagnosisQueryService queryService;

    public DiagnosisController(DiagnosisCommandService commandService,
                               DiagnosisQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(
            summary = "Create a diagnosis",
            description = "Creates a medical diagnosis for a patient."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Diagnosis created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiagnosisResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77",
                                      "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                      "doctorId": "2ee7a314-9b2f-4f8e-8c31-73b3cdd0b221",
                                      "description": "Paciente presenta niveles elevados de glucosa en ayunas",
                                      "recommendation": "Se recomienda control nutricional y monitoreo diario de glucosa",
                                      "status": "DRAFT",
                                      "createdAt": "2026-06-17T09:20:00",
                                      "updatedAt": "2026-06-17T09:20:00"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateDiagnosisRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(
                        request.description(),
                        request.recommendation()),
                DiagnosisResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get diagnoses",
            description = "Returns all diagnoses or filters them by patient identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Diagnoses found",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DiagnosisResource.class))
                    )
            )
    })
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<DiagnosisResource>> getDiagnoses(
            @Parameter(
                    description = "Patient identifier used to filter diagnoses",
                    example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101"
            )
            @RequestParam(required = false) UUID patientId) {
        var diagnoses = patientId == null
                ? queryService.findAll()
                : queryService.findByPatientId(PatientId.of(patientId));

        return ResponseEntity.ok(
                diagnoses.stream()
                        .map(DiagnosisResource::fromDomain)
                        .toList());
    }
}