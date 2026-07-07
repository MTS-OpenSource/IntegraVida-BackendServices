package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Doctor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "Doctor", description = "Doctor profile linked to a platform profile")
public record DoctorResource(UUID id,
                             @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101")
                             UUID profileId,
                             @Schema(example = "DOC-000123")
                             String doctorRecordNumber,
                             @Schema(example = "Cardiologist specializing in diabetes care.")
                             String notes,
                             @Schema(example = "true")
                             boolean active,
                             @Schema(example = "2026-06-13T08:30:01")
                             LocalDateTime createdAt,
                             @Schema(example = "2026-06-13T10:15:01")
                             LocalDateTime updatedAt) {
    public static DoctorResource fromDomain(Doctor doctor) {
        return new DoctorResource(
                doctor.getId().value(),
                doctor.getProfileId(),
                doctor.getDoctorRecordNumber(),
                doctor.getNotes(),
                doctor.isActive(),
                doctor.getCreatedAt(),
                doctor.getUpdatedAt());
    }
}
