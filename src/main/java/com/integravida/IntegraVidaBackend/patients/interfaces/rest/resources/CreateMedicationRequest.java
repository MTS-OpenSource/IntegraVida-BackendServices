package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "CreateMedicationRequest", description = "Payload to create a medication")
public record CreateMedicationRequest(
        @NotNull @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @NotNull @Schema(example = "2b2f7f3f-3d8a-4e6d-9c55-2f4f5b6c7d8e") UUID treatmentId,
        @NotBlank @Schema(example = "Metformin") String name,
        @NotBlank @Schema(example = "500 mg") String dosage,
        @NotNull @Schema(example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]") List<DayOfWeek> daysOfWeek,
        @NotNull @Schema(example = "[\"08:00\", \"20:00\"]") List<LocalTime> doseTimes,
        @NotBlank @Schema(example = "Take with meals") String instructions) {
}
