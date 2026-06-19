package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.MedicationIntake;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "MedicationIntake", description = "Record of a medication intake")
public record MedicationIntakeResource(UUID id,
                                       @Schema(example = "2b2f7f3f-3d8a-4e6d-9c55-2f4f5b6c7d8e")
                                       UUID medicationId,
                                       @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101")
                                       UUID patientId,
                                       @Schema(example = "2026-06-13T08:00:00")
                                       LocalDateTime takenAt,
                                       @Schema(example = "Taken after breakfast")
                                       String notes,
                                       @Schema(example = "2026-06-13T08:05:00")
                                       LocalDateTime createdAt) {
    public static MedicationIntakeResource fromDomain(MedicationIntake medicationIntake) {
        return new MedicationIntakeResource(
                medicationIntake.getId(),
                medicationIntake.getMedicationId(),
                medicationIntake.getPatientId().value(),
                medicationIntake.getTakenAt(),
                medicationIntake.getNotes(),
                medicationIntake.getCreatedAt());
    }
}
