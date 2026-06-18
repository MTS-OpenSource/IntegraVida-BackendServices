package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient;

import java.time.LocalDateTime;
import java.util.UUID;

public record PatientResource(UUID id,
                              UUID profileId,
                              String fullName,
                              String email,
                              String medicalRecordNumber,
                              String notes,
                              boolean active,
                              LocalDateTime createdAt,
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
