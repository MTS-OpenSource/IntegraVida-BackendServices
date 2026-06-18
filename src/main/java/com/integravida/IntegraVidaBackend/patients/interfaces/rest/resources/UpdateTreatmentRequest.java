package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateTreatmentRequest(@NotBlank String name,
                                     @NotBlank String description,
                                     @NotNull LocalDate startDate,
                                     LocalDate endDate,
                                     String status) {
}
