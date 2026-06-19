package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "UpdatePatientRequest", description = "Payload to update patient notes")
public record UpdatePatientRequest(@NotBlank @Schema(example = "Updated notes with follow-up plan.") String notes) {
}
