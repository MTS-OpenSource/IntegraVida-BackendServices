package com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CreateClinicalReportRequest", description = "Payload to create a clinical report")
public record CreateClinicalReportRequest(
        @NotBlank @Schema(example = "Reporte clínico mensual") String title,
        @NotBlank @Schema(example = "El paciente presenta una evolución estable durante el último mes") String summary,
        @NotBlank @Schema(example = "Continuar con el monitoreo diario de glucosa y mantener el plan nutricional") String recommendations) {
}