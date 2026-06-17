package com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(name = "UpdateAppointmentRequest", description = "Payload to update or reschedule a medical appointment")
public record UpdateAppointmentRequest(
        @NotNull @Schema(example = "2026-06-21T11:00:00") LocalDateTime scheduledAt,
        @NotBlank @Schema(example = "Reprogramación por disponibilidad del paciente") String reason) {
}