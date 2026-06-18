package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Medication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MedicationResource(UUID id,
                                 UUID patientId,
                                 UUID treatmentId,
                                 String name,
                                 String dosage,
                                 List<String> daysOfWeek,
                                 List<String> doseTimes,
                                 String instructions,
                                 boolean active,
                                 LocalDateTime discontinuedAt,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
    public static MedicationResource fromDomain(Medication medication) {
        return new MedicationResource(
                medication.getId(),
                medication.getPatientId().value(),
                medication.getTreatmentId(),
                medication.getName(),
                medication.getDosage(),
                medication.getSchedule().daysOfWeek().stream().map(Enum::name).toList(),
                medication.getSchedule().doseTimes().stream().map(java.time.LocalTime::toString).toList(),
                medication.getSchedule().instructions(),
                medication.isActive(),
                medication.getDiscontinuedAt(),
                medication.getCreatedAt(),
                medication.getUpdatedAt());
    }
}
