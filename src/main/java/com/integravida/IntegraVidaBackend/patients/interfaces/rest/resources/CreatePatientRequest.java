package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePatientRequest(@NotNull UUID profileId,
                                   @NotBlank String medicalRecordNumber,
                                   @NotBlank String notes) {
}
