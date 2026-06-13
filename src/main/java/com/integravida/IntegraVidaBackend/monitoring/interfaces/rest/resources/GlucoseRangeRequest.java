package com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "GlucoseRangeRequest", description = "Payload to create or update a glucose range")
public record GlucoseRangeRequest(
        @NotNull @Schema(example = "75") BigDecimal minimumValue,
        @NotNull @Schema(example = "170") BigDecimal maximumValue) {
}
