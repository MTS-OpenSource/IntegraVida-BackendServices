package com.integravida.IntegraVidaBackend.medical.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.medical.application.services.AppointmentCommandService;
import com.integravida.IntegraVidaBackend.medical.application.services.AppointmentQueryService;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.AppointmentResource;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.CreateAppointmentRequest;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.UpdateAppointmentRequest;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "Medical - Appointments", description = "Medical appointment management endpoints")
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentCommandService commandService;
    private final AppointmentQueryService queryService;

    public AppointmentController(AppointmentCommandService commandService,
                                 AppointmentQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(
            summary = "Create an appointment",
            description = "Creates a new medical appointment for a patient and doctor."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77",
                                      "patientId": "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101",
                                      "doctorId": "2ee7a314-9b2f-4f8e-8c31-73b3cdd0b221",
                                      "scheduledAt": "2026-06-20T10:30:00",
                                      "status": "PENDING",
                                      "reason": "Revisión de resultados de laboratorio",
                                      "createdAt": "2026-06-17T09:20:00",
                                      "updatedAt": "2026-06-17T09:20:00",
                                      "cancelledAt": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateAppointmentRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(
                        PatientId.of(request.patientId()),
                        DoctorId.of(request.doctorId()),
                        request.scheduledAt(),
                        request.reason()),
                AppointmentResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get appointments",
            description = "Returns all appointments or filters them by patient identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointments found",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AppointmentResource.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<AppointmentResource>> getAppointments(
            @Parameter(description = "Patient identifier used to filter appointments",
                    example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101")
            @RequestParam(required = false) UUID patientId) {
        var appointments = patientId == null
                ? queryService.findAll()
                : queryService.findByPatientId(PatientId.of(patientId));

        return ResponseEntity.ok(
                appointments.stream()
                        .map(AppointmentResource::fromDomain)
                        .toList());
    }

    @Operation(
            summary = "Get an appointment by id",
            description = "Returns the details of a single medical appointment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "Appointment identifier",
                    example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(id),
                AppointmentResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(
            summary = "Update an appointment",
            description = "Updates the date, time and reason of an existing appointment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment updated"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "Appointment identifier",
                    example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77")
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAppointmentRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.update(id, request.scheduledAt(), request.reason()),
                AppointmentResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(
            summary = "Delete an appointment",
            description = "Deletes a medical appointment permanently."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResource.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "Appointment deleted: 9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "Appointment identifier",
                    example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77")
            @PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.delete(id),
                deletedId -> new MessageResource("Appointment deleted: " + deletedId),
                HttpStatus.OK);
    }
}