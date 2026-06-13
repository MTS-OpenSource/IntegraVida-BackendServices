package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "CreateGlucoseRecordRequest", description = "Payload to create a glucose record")
public record CreateGlucoseRecordRequest(
        @NotNull @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @NotNull @Schema(example = "245.5") BigDecimal glucoseValue,
        @NotNull @Schema(example = "2026-06-13T08:30:00") LocalDateTime measuredAt) {
}
