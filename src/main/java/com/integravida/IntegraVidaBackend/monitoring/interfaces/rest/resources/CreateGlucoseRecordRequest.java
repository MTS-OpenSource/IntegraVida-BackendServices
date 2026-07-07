package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "CreateGlucoseRecordRequest", description = "Payload to create a glucose record")
public record CreateGlucoseRecordRequest(
        @NotNull @Schema(example = "245.5") BigDecimal glucoseValue,
        @NotNull @Schema(example = "2026-06-13T08:30:00") LocalDateTime measuredAt) {
}
