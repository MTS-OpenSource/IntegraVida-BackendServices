package com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(name = "CreateAppointmentRequest", description = "Payload to create a medical appointment")
public record CreateAppointmentRequest(
        @NotNull @Schema(example = "2026-06-20T10:30:00") LocalDateTime scheduledAt,
        @NotBlank @Schema(example = "Revisión de resultados de laboratorio") String reason) {
}