package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.ClinicalObservation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "ClinicalObservation", description = "Clinical note or structured observation")
public record ClinicalObservationResource(
        @Schema(example = "5a3f2b11-47a6-4d8d-8f83-6f47d4f7e501") UUID id,
        @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @Schema(example = "note") String category,
        @Schema(example = "Post-meal review") String title,
        @Schema(example = "Patient reported dizziness after lunch.") String content,
        @Schema(example = "2026-06-13T09:30:00") LocalDateTime observedAt,
        @Schema(example = "2026-06-13T09:31:00") LocalDateTime createdAt,
        @Schema(example = "2026-06-13T10:20:00") LocalDateTime updatedAt) {

    public static ClinicalObservationResource fromDomain(ClinicalObservation observation) {
        return new ClinicalObservationResource(
                observation.getId(),
                observation.getPatientId().value(),
                observation.getCategory(),
                observation.getTitle(),
                observation.getContent(),
                observation.getObservedAt(),
                observation.getCreatedAt(),
                observation.getUpdatedAt());
    }
}
