package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CreateDoctorRequest", description = "Payload to create a doctor")
public record CreateDoctorRequest(
        @NotBlank @Schema(example = "DOC-000123") String doctorRecordNumber,
        @NotBlank @Schema(example = "Cardiologist specializing in diabetes care.") String notes) {
}
