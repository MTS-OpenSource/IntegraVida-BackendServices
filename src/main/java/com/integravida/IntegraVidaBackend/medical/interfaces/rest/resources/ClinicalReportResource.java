package com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.ClinicalReport;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "ClinicalReport", description = "Clinical report resource")
public record ClinicalReportResource(
        @Schema(example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77") UUID id,
        @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @Schema(example = "2ee7a314-9b2f-4f8e-8c31-73b3cdd0b221") UUID doctorId,
        @Schema(example = "Reporte clínico mensual") String title,
        @Schema(example = "El paciente presenta una evolución estable durante el último mes") String summary,
        @Schema(example = "Continuar con el monitoreo diario de glucosa y mantener el plan nutricional") String recommendations,
        @Schema(example = "DRAFT") String status,
        @Schema(example = "2026-06-17T09:20:00") LocalDateTime issuedAt,
        @Schema(example = "2026-06-17T09:20:00") LocalDateTime createdAt,
        @Schema(example = "2026-06-17T09:20:00") LocalDateTime updatedAt) {

    public static ClinicalReportResource fromDomain(ClinicalReport clinicalReport) {
        return new ClinicalReportResource(
                clinicalReport.getId(),
                clinicalReport.getPatientId().value(),
                clinicalReport.getDoctorId().value(),
                clinicalReport.getTitle(),
                clinicalReport.getSummary(),
                clinicalReport.getRecommendations(),
                clinicalReport.getStatus().name(),
                clinicalReport.getIssuedAt().orElse(null),
                clinicalReport.getCreatedAt(),
                clinicalReport.getUpdatedAt());
    }
}