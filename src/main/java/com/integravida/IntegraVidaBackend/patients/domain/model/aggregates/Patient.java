package com.integravida.IntegraVidaBackend.patients.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Patient extends AbstractDomainAggregateRoot<Patient> {
    private final PatientId id;
    private final UUID profileId;
    private final String medicalRecordNumber;
    private String notes;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Patient(PatientId id,
                    UUID profileId,
                    String medicalRecordNumber,
                    String notes,
                    boolean active,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.profileId = Objects.requireNonNull(profileId, "profileId is required");
        this.medicalRecordNumber = requireText(medicalRecordNumber, "medicalRecordNumber");
        this.notes = requireText(notes, "notes");
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public static Patient create(PatientId id,
                                 UUID profileId,
                                 String medicalRecordNumber,
                                 String notes,
                                 LocalDateTime createdAt) {
        return new Patient(id, profileId, medicalRecordNumber, notes, true, createdAt, createdAt);
    }

    public static Patient reconstitute(PatientId id,
                                       UUID profileId,
                                       String medicalRecordNumber,
                                       String notes,
                                       boolean active,
                                       LocalDateTime createdAt,
                                       LocalDateTime updatedAt) {
        return new Patient(id, profileId, medicalRecordNumber, notes, active, createdAt, updatedAt);
    }

    public void updateNotes(String notes, LocalDateTime updatedAt) {
        this.notes = requireText(notes, "notes");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void deactivate(LocalDateTime updatedAt) {
        this.active = false;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void reactivate(LocalDateTime updatedAt) {
        this.active = true;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public PatientId getId() {
        return id;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public String getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private static String requireText(String value, String field) {
        var text = Objects.requireNonNull(value, field + " is required");
        if (text.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return text;
    }
}
