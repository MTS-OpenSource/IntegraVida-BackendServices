package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.monitoring.application.services.GlucoseRecordCommandService;
import com.integravida.IntegraVidaBackend.monitoring.application.services.GlucoseRecordQueryService;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.CreateGlucoseRecordRequest;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.GlucoseRecordResource;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.UpdateGlucoseRecordRequest;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Monitoring - Glucose Records", description = "CRUD and date filtering for glucose records")
@RestController
@RequestMapping("/api/v1/glucose-records")
public class GlucoseRecordController {
    private final GlucoseRecordCommandService commandService;
    private final GlucoseRecordQueryService queryService;

    public GlucoseRecordController(GlucoseRecordCommandService commandService, GlucoseRecordQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(
            summary = "Create a glucose record",
            description = "Stores a new glucose measurement for a patient and triggers an alert when the value is outside the active range."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Glucose record created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlucoseRecordResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77",
                                      "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                      "glucoseValue": 245.5,
                                      "minimumRange": 70,
                                      "maximumRange": 180,
                                      "triggeredSeverity": "CRITICAL",
                                      "measuredAt": "2026-06-13T08:30:00",
                                      "createdAt": "2026-06-13T08:30:01",
                                      "updatedAt": "2026-06-13T08:30:01"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PostMapping
    public ResponseEntity<?> create(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Glucose record payload",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateGlucoseRecordRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                      "glucoseValue": 245.5,
                                      "measuredAt": "2026-06-13T08:30:00"
                                    }
                                    """)
                    )
            )
            @RequestBody CreateGlucoseRecordRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(PatientId.of(request.patientId()), GlucoseValue.of(request.glucoseValue()), request.measuredAt()),
                GlucoseRecordResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get a glucose record by id",
            description = "Returns the detailed information of a single glucose record."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Glucose record found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlucoseRecordResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77",
                                      "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                      "glucoseValue": 245.5,
                                      "minimumRange": 70,
                                      "maximumRange": 180,
                                      "triggeredSeverity": "CRITICAL",
                                      "measuredAt": "2026-06-13T08:30:00",
                                      "createdAt": "2026-06-13T08:30:01",
                                      "updatedAt": "2026-06-13T08:30:01"
                                    }
                                    """)
                    )
            ),

    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "Glucose record identifier", example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(id),
                GlucoseRecordResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(
            summary = "List glucose records by patient",
            description = "Returns all records for a patient, optionally filtered by measuredAt range."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Glucose records found",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GlucoseRecordResource.class)),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77",
                                        "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                        "glucoseValue": 245.5,
                                        "minimumRange": 70,
                                        "maximumRange": 180,
                                        "triggeredSeverity": "CRITICAL",
                                        "measuredAt": "2026-06-13T08:30:00",
                                        "createdAt": "2026-06-13T08:30:01",
                                        "updatedAt": "2026-06-13T08:30:01"
                                      }
                                    ]
                                    """)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<GlucoseRecordResource>> findByPatientId(
            @Parameter(description = "Patient identifier", example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101")
            @RequestParam UUID patientId,
            @Parameter(description = "Inclusive lower bound for measuredAt", example = "2026-06-13T00:00:00")
            @RequestParam(required = false) LocalDateTime from,
            @Parameter(description = "Inclusive upper bound for measuredAt", example = "2026-06-13T23:59:59")
            @RequestParam(required = false) LocalDateTime to) {
        var records = (from != null && to != null)
                ? queryService.findByPatientIdAndMeasuredAtBetween(PatientId.of(patientId), from, to)
                : queryService.findByPatientId(PatientId.of(patientId));
        return ResponseEntity.ok(records.stream().map(GlucoseRecordResource::fromDomain).toList());
    }

    @Operation(
            summary = "Update a glucose record",
            description = "Updates the glucose value and measuredAt timestamp for an existing record."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Glucose record updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlucoseRecordResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77",
                                      "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                      "glucoseValue": 128.0,
                                      "minimumRange": 70,
                                      "maximumRange": 180,
                                      "triggeredSeverity": null,
                                      "measuredAt": "2026-06-13T10:15:00",
                                      "createdAt": "2026-06-13T08:30:01",
                                      "updatedAt": "2026-06-13T10:15:01"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Glucose record not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "Glucose record identifier", example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77")
            @PathVariable UUID id,
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated glucose record payload",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateGlucoseRecordRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "glucoseValue": 128.0,
                                      "measuredAt": "2026-06-13T10:15:00"
                                    }
                                    """)
                    )
            )
            @RequestBody UpdateGlucoseRecordRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.update(id, GlucoseValue.of(request.glucoseValue()), request.measuredAt()),
                GlucoseRecordResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a glucose record",
            description = "Deletes a glucose record permanently."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Glucose record deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "Glucose record deleted: 9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Glucose record not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "Glucose record identifier", example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.delete(id),
                deletedId -> new MessageResource("Glucose record deleted: " + deletedId),
                HttpStatus.OK);
    }
}
