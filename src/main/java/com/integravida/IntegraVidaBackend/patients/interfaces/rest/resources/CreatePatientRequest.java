package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(name = "CreatePatientRequest", description = "Payload to create a patient")
public record CreatePatientRequest(
        @NotNull @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID profileId,
        @NotBlank @Schema(example = "MRN-000123") String medicalRecordNumber,
        @NotBlank @Schema(example = "Patient has type 2 diabetes and needs follow-up.") String notes) {
}
