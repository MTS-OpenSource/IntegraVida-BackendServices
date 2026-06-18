package com.integravida.IntegraVidaBackend.medical.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.medical.application.services.ClinicalReportCommandService;
import com.integravida.IntegraVidaBackend.medical.application.services.ClinicalReportQueryService;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.ClinicalReportResource;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.CreateClinicalReportRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Medical - Clinical Reports", description = "Clinical report management endpoints")
@RestController
@RequestMapping("/api/v1/clinical-reports")
public class ClinicalReportController {
    private final ClinicalReportCommandService commandService;
    private final ClinicalReportQueryService queryService;

    public ClinicalReportController(ClinicalReportCommandService commandService,
                                    ClinicalReportQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(
            summary = "Create a clinical report",
            description = "Creates a clinical report for a patient."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Clinical report created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClinicalReportResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77",
                                      "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                      "doctorId": "2ee7a314-9b2f-4f8e-8c31-73b3cdd0b221",
                                      "title": "Reporte clínico mensual",
                                      "summary": "El paciente presenta una evolución estable durante el último mes",
                                      "recommendations": "Continuar con el monitoreo diario de glucosa y mantener el plan nutricional",
                                      "status": "DRAFT",
                                      "issuedAt": null,
                                      "createdAt": "2026-06-17T09:20:00",
                                      "updatedAt": "2026-06-17T09:20:00"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateClinicalReportRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(
                        PatientId.of(request.patientId()),
                        DoctorId.of(request.doctorId()),
                        request.title(),
                        request.summary(),
                        request.recommendations()),
                ClinicalReportResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get clinical reports",
            description = "Returns all clinical reports or filters them by patient identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Clinical reports found",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClinicalReportResource.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<ClinicalReportResource>> getClinicalReports(
            @Parameter(
                    description = "Patient identifier used to filter clinical reports",
                    example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101"
            )
            @RequestParam(required = false) UUID patientId) {
        var clinicalReports = patientId == null
                ? queryService.findAll()
                : queryService.findByPatientId(PatientId.of(patientId));

        return ResponseEntity.ok(
                clinicalReports.stream()
                        .map(ClinicalReportResource::fromDomain)
                        .toList());
    }
}