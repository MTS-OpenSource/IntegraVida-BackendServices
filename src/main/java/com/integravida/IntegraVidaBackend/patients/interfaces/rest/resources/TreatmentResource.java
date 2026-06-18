package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Treatment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TreatmentResource(UUID id,
                                UUID patientId,
                                String name,
                                String description,
                                LocalDate startDate,
                                LocalDate endDate,
                                String status,
                                LocalDateTime createdAt,
                                LocalDateTime updatedAt) {
    public static TreatmentResource fromDomain(Treatment treatment) {
        return new TreatmentResource(
                treatment.getId(),
                treatment.getPatientId().value(),
                treatment.getName(),
                treatment.getDescription(),
                treatment.getStartDate(),
                treatment.getEndDate(),
                treatment.getStatus().name(),
                treatment.getCreatedAt(),
                treatment.getUpdatedAt());
    }
}
