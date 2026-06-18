package com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Diagnosis;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "Diagnosis", description = "Medical diagnosis resource")
public record DiagnosisResource(
        @Schema(example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77") UUID id,
        @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @Schema(example = "2ee7a314-9b2f-4f8e-8c31-73b3cdd0b221") UUID doctorId,
        @Schema(example = "Paciente presenta niveles elevados de glucosa en ayunas") String description,
        @Schema(example = "Se recomienda control nutricional y monitoreo diario de glucosa") String recommendation,
        @Schema(example = "DRAFT") String status,
        @Schema(example = "2026-06-17T09:20:00") LocalDateTime createdAt,
        @Schema(example = "2026-06-17T09:20:00") LocalDateTime updatedAt) {

    public static DiagnosisResource fromDomain(Diagnosis diagnosis) {
        return new DiagnosisResource(
                diagnosis.getId(),
                diagnosis.getPatientId().value(),
                diagnosis.getDoctorId().value(),
                diagnosis.getDescription(),
                diagnosis.getRecommendation(),
                diagnosis.getStatus().name(),
                diagnosis.getCreatedAt(),
                diagnosis.getUpdatedAt());
    }
}