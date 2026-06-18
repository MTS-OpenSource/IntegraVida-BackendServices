package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTreatmentRequest(@NotNull UUID patientId,
                                     @NotBlank String name,
                                     @NotBlank String description,
                                     @NotNull LocalDate startDate,
                                     LocalDate endDate) {
}
