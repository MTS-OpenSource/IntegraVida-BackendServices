package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRange;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "GlucoseRange", description = "Patient-specific glucose target range")
public record GlucoseRangeResource(
        @Schema(example = "d12a2f9e-2d64-40b6-8f65-2b84f0f42010") UUID id,
        @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @Schema(example = "75") BigDecimal minimumValue,
        @Schema(example = "170") BigDecimal maximumValue,
        @Schema(example = "true") boolean active,
        @Schema(example = "2026-06-13T08:00:00") LocalDateTime createdAt,
        @Schema(example = "2026-06-13T10:00:00") LocalDateTime updatedAt) {

    public static GlucoseRangeResource fromDomain(GlucoseRange range) {
        return new GlucoseRangeResource(
                range.getId(),
                range.getPatientId().value(),
                range.getMinimumValue().value(),
                range.getMaximumValue().value(),
                range.isActive(),
                range.getCreatedAt(),
                range.getUpdatedAt());
    }
}
