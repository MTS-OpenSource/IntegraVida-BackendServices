package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateMedicationIntakeRequest(@NotNull UUID medicationId,
                                            @NotNull UUID patientId,
                                            @NotNull LocalDateTime takenAt,
                                            @NotBlank String notes) {
}
