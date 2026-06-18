package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.MedicationIntake;

import java.time.LocalDateTime;
import java.util.UUID;

public record MedicationIntakeResource(UUID id,
                                       UUID medicationId,
                                       UUID patientId,
                                       LocalDateTime takenAt,
                                       String notes,
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
