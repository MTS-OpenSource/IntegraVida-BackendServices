package com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CreateDiagnosisRequest", description = "Payload to create a medical diagnosis")
public record CreateDiagnosisRequest(
        @NotBlank @Schema(example = "Paciente presenta niveles elevados de glucosa en ayunas") String description,
        @NotBlank @Schema(example = "Se recomienda control nutricional y monitoreo diario de glucosa") String recommendation) {
}