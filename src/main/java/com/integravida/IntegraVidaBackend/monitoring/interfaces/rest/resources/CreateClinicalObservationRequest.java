package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(name = "CreateClinicalObservationRequest", description = "Payload to create a clinical observation")
public record CreateClinicalObservationRequest(
        @NotNull @Schema(example = "note") String category,
        @NotNull @Schema(example = "Post-meal review") String title,
        @NotNull @Schema(example = "Patient reported dizziness after lunch.") String content,
        @NotNull @Schema(example = "2026-06-13T09:30:00") LocalDateTime observedAt) {
}
