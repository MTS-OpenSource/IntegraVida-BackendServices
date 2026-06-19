package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "CreateMedicationIntakeRequest", description = "Payload to register a medication intake")
public record CreateMedicationIntakeRequest(
        @NotNull @Schema(example = "2b2f7f3f-3d8a-4e6d-9c55-2f4f5b6c7d8e") UUID medicationId,
        @NotNull @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @NotNull @Schema(example = "2026-06-13T08:00:00") LocalDateTime takenAt,
        @NotBlank @Schema(example = "Taken after breakfast") String notes) {
}
