package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "UpdateGlucoseRecordRequest", description = "Payload to update a glucose record")
public record UpdateGlucoseRecordRequest(
        @NotNull @Schema(example = "128.0") BigDecimal glucoseValue,
        @NotNull @Schema(example = "2026-06-13T10:15:00") LocalDateTime measuredAt) {
}
