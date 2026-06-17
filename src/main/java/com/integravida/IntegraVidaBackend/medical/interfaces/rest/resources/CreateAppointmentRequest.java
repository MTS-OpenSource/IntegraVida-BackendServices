package com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "CreateAppointmentRequest", description = "Payload to create a medical appointment")
public record CreateAppointmentRequest(
        @NotNull @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @NotNull @Schema(example = "2ee7a314-9b2f-4f8e-8c31-73b3cdd0b221") UUID doctorId,
        @NotNull @Schema(example = "2026-06-20T10:30:00") LocalDateTime scheduledAt,
        @NotBlank @Schema(example = "Revisión de resultados de laboratorio") String reason) {
}