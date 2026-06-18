package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record CreateMedicationRequest(@NotNull UUID patientId,
                                      @NotNull UUID treatmentId,
                                      @NotBlank String name,
                                      @NotBlank String dosage,
                                      @NotNull List<DayOfWeek> daysOfWeek,
                                      @NotNull List<LocalTime> doseTimes,
                                      @NotBlank String instructions) {
}
