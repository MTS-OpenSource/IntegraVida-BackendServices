package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.monitoring.application.services.AlertCommandService;
import com.integravida.IntegraVidaBackend.monitoring.application.services.AlertQueryService;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.AlertResource;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Monitoring - Alerts", description = "Alert lookup and read state updates")
@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {
    private final AlertCommandService commandService;
    private final AlertQueryService queryService;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public AlertController(AlertCommandService commandService, AlertQueryService queryService, JwtClaimsExtractor jwtClaimsExtractor) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Operation(summary = "Get an alert by id", description = "Returns the details of a single alert.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Alert found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlertResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "7fda5e8f-35b8-4e8d-8d1f-40d7f9f0a111",
                                      "glucoseRecordId": "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77",
                                      "patientId": 1,
                                      "glucoseValue": 245.5,
                                      "severity": "CRITICAL",
                                      "message": "Glucose value 245.5 is outside the target range [70, 180]",
                                      "read": false,
                                      "createdAt": "2026-06-13T08:30:00",
                                      "readAt": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "Alert identifier", example = "7fda5e8f-35b8-4e8d-8d1f-40d7f9f0a111")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(id),
                AlertResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(summary = "List alerts for the authenticated patient", description = "Returns all alerts for the patient from the JWT, optionally filtered to unread alerts only.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Alerts found",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AlertResource.class)),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": "7fda5e8f-35b8-4e8d-8d1f-40d7f9f0a111",
                                        "glucoseRecordId": "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77",
                                        "patientId": 1,
                                        "glucoseValue": 245.5,
                                        "severity": "CRITICAL",
                                        "message": "Glucose value 245.5 is outside the target range [70, 180]",
                                        "read": false,
                                        "createdAt": "2026-06-13T08:30:00",
                                        "readAt": null
                                      }
                                    ]
                                    """)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<AlertResource>> findByPatientId(
            @Parameter(description = "When true, returns only unread alerts", example = "false")
            @RequestParam(required = false, defaultValue = "false") boolean unreadOnly) {
        var patientId = PatientId.fromString(jwtClaimsExtractor.extractPatientId());
        var alerts = unreadOnly
                ? queryService.findUnreadByPatientId(patientId)
                : queryService.findByPatientId(patientId);
        return ResponseEntity.ok(alerts.stream().map(AlertResource::fromDomain).toList());
    }

    @Operation(summary = "Mark an alert as read", description = "Sets readAt for the alert and returns the updated resource.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Alert updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlertResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "7fda5e8f-35b8-4e8d-8d1f-40d7f9f0a111",
                                      "glucoseRecordId": "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77",
                                      "patientId": 1,
                                      "glucoseValue": 245.5,
                                      "severity": "CRITICAL",
                                      "message": "Glucose value 245.5 is outside the target range [70, 180]",
                                      "read": true,
                                      "createdAt": "2026-06-13T08:30:00",
                                      "readAt": "2026-06-13T09:00:00"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @Parameter(description = "Alert identifier", example = "7fda5e8f-35b8-4e8d-8d1f-40d7f9f0a111")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.markAsRead(id),
                AlertResource::fromDomain,
                HttpStatus.OK);
    }
}
