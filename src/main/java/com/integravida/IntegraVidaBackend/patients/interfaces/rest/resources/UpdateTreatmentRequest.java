package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(name = "UpdateTreatmentRequest", description = "Payload to update a treatment")
public record UpdateTreatmentRequest(
        @NotBlank @Schema(example = "Metformin plan") String name,
        @NotBlank @Schema(example = "Daily glucose control plan") String description,
        @NotNull @Schema(example = "2026-06-01") LocalDate startDate,
        @Schema(example = "2026-12-01") LocalDate endDate,
        @Schema(example = "ACTIVE") String status) {
}
