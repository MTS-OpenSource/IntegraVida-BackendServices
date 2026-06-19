package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "Patient", description = "Patient profile linked to a platform profile")
public record PatientResource(UUID id,
                              @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101")
                              UUID profileId,
                              @Schema(example = "Ana Pérez")
                              String fullName,
                              @Schema(example = "ana.perez@integravida.com")
                              String email,
                              @Schema(example = "MRN-000123")
                              String medicalRecordNumber,
                              @Schema(example = "Patient has type 2 diabetes and needs follow-up.")
                              String notes,
                              @Schema(example = "true")
                              boolean active,
                              @Schema(example = "2026-06-13T08:30:01")
                              LocalDateTime createdAt,
                              @Schema(example = "2026-06-13T10:15:01")
                              LocalDateTime updatedAt) {
    public static PatientResource fromDomain(Patient patient, String fullName, String email) {
        return new PatientResource(
                patient.getId().value(),
                patient.getProfileId(),
                fullName,
                email,
                patient.getMedicalRecordNumber(),
                patient.getNotes(),
                patient.isActive(),
                patient.getCreatedAt(),
                patient.getUpdatedAt());
    }
}
