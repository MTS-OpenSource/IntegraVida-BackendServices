package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(name = "UpdateClinicalObservationRequest", description = "Payload to update a clinical observation")
public record UpdateClinicalObservationRequest(
        @NotNull @Schema(example = "report") String category,
        @NotNull @Schema(example = "Updated review") String title,
        @NotNull @Schema(example = "Updated clinical summary.") String content,
        @NotNull @Schema(example = "2026-06-13T10:15:00") LocalDateTime observedAt) {
}
