package com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(name = "CreateDiagnosisRequest", description = "Payload to create a medical diagnosis")
public record CreateDiagnosisRequest(
        @NotNull @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @NotNull @Schema(example = "2ee7a314-9b2f-4f8e-8c31-73b3cdd0b221") UUID doctorId,
        @NotBlank @Schema(example = "Paciente presenta niveles elevados de glucosa en ayunas") String description,
        @NotBlank @Schema(example = "Se recomienda control nutricional y monitoreo diario de glucosa") String recommendation) {
}