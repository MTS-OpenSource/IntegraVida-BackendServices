package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.Alert;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "Alert", description = "Automatic alert generated from an abnormal glucose record")
public record AlertResource(
        @Schema(example = "7fda5e8f-35b8-4e8d-8d1f-40d7f9f0a111") UUID id,
        @Schema(example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77") UUID glucoseRecordId,
        @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @Schema(example = "245.5") BigDecimal glucoseValue,
        @Schema(example = "CRITICAL") String severity,
        @Schema(example = "Glucose value 245.5 is outside the target range [70, 180]") String message,
        @Schema(example = "false") boolean read,
        @Schema(example = "2026-06-13T08:30:00") LocalDateTime createdAt,
        @Schema(example = "2026-06-13T09:00:00") LocalDateTime readAt) {

    public static AlertResource fromDomain(Alert alert) {
        return new AlertResource(
                alert.getId(),
                alert.getGlucoseRecordId(),
                alert.getPatientId().value(),
                alert.getGlucoseValue().value(),
                alert.getSeverity().name(),
                alert.getMessage(),
                alert.isRead(),
                alert.getCreatedAt(),
                alert.getReadAt().orElse(null));
    }
}
