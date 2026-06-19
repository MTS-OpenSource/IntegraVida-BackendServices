package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "CreateTreatmentRequest", description = "Payload to create a treatment")
public record CreateTreatmentRequest(
        @NotNull @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @NotBlank @Schema(example = "Metformin plan") String name,
        @NotBlank @Schema(example = "Daily glucose control plan") String description,
        @NotNull @Schema(example = "2026-06-01") LocalDate startDate,
        @Schema(example = "2026-12-01") LocalDate endDate) {
}
