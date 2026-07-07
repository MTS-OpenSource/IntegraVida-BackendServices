package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(name = "CreatePatientRequest", description = "Payload to create a patient")
public record CreatePatientRequest(
        @NotBlank @Schema(example = "MRN-000123") String medicalRecordNumber,
        @NotBlank @Schema(example = "Patient has type 2 diabetes and needs follow-up.") String notes) {
}
