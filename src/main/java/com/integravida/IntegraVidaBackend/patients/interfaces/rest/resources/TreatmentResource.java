package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Treatment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "Treatment", description = "Treatment assigned to a patient")
public record TreatmentResource(UUID id,
                                @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101")
                                UUID patientId,
                                @Schema(example = "Metformin plan")
                                String name,
                                @Schema(example = "Daily glucose control plan")
                                String description,
                                @Schema(example = "2026-06-01")
                                LocalDate startDate,
                                @Schema(example = "2026-12-01")
                                LocalDate endDate,
                                @Schema(example = "ACTIVE")
                                String status,
                                @Schema(example = "2026-06-13T08:30:01")
                                LocalDateTime createdAt,
                                @Schema(example = "2026-06-13T10:15:01")
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
