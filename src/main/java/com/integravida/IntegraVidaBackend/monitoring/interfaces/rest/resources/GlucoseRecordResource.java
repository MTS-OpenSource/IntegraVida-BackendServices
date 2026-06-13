package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRecord;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "GlucoseRecord", description = "Glucose measurement resource")
public record GlucoseRecordResource(
        @Schema(example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77") UUID id,
        @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @Schema(example = "245.5") BigDecimal glucoseValue,
        @Schema(example = "70") BigDecimal minimumRange,
        @Schema(example = "180") BigDecimal maximumRange,
        @Schema(example = "CRITICAL") String triggeredSeverity,
        @Schema(example = "2026-06-13T08:30:00") LocalDateTime measuredAt,
        @Schema(example = "2026-06-13T08:30:01") LocalDateTime createdAt,
        @Schema(example = "2026-06-13T08:30:01") LocalDateTime updatedAt) {

    public static GlucoseRecordResource fromDomain(GlucoseRecord record) {
        return new GlucoseRecordResource(
                record.getId(),
                record.getPatientId().value(),
                record.getGlucoseValue().value(),
                record.getMinimumRange().value(),
                record.getMaximumRange().value(),
                record.getTriggeredSeverity().map(Enum::name).orElse(null),
                record.getMeasuredAt(),
                record.getCreatedAt(),
                record.getUpdatedAt());
    }
}
