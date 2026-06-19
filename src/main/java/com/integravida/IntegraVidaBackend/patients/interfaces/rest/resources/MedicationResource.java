package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Medication;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "Medication", description = "Medication assigned to a treatment")
public record MedicationResource(UUID id,
                                 @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101")
                                 UUID patientId,
                                 @Schema(example = "2b2f7f3f-3d8a-4e6d-9c55-2f4f5b6c7d8e")
                                 UUID treatmentId,
                                 @Schema(example = "Metformin")
                                 String name,
                                 @Schema(example = "500 mg")
                                 String dosage,
                                 @Schema(example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]")
                                 List<String> daysOfWeek,
                                 @Schema(example = "[\"08:00\", \"20:00\"]")
                                 List<String> doseTimes,
                                 @Schema(example = "Take with meals")
                                 String instructions,
                                 @Schema(example = "true")
                                 boolean active,
                                 @Schema(example = "2026-06-13T10:15:01")
                                 LocalDateTime discontinuedAt,
                                 @Schema(example = "2026-06-13T08:30:01")
                                 LocalDateTime createdAt,
                                 @Schema(example = "2026-06-13T10:15:01")
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
