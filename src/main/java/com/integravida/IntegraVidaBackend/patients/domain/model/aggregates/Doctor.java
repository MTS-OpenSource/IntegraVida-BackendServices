package com.integravida.IntegraVidaBackend.patients.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Doctor extends AbstractDomainAggregateRoot<Doctor> {
    private final DoctorId id;
    private final UUID profileId;
    private final String doctorRecordNumber;
    private String notes;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Doctor(DoctorId id,
                   UUID profileId,
                   String doctorRecordNumber,
                   String notes,
                   boolean active,
                   LocalDateTime createdAt,
                   LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.profileId = Objects.requireNonNull(profileId, "profileId is required");
        this.doctorRecordNumber = requireText(doctorRecordNumber, "doctorRecordNumber");
        this.notes = requireText(notes, "notes");
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public static Doctor create(DoctorId id,
                                UUID profileId,
                                String doctorRecordNumber,
                                String notes,
                                LocalDateTime createdAt) {
        return new Doctor(id, profileId, doctorRecordNumber, notes, true, createdAt, createdAt);
    }

    public static Doctor reconstitute(DoctorId id,
                                      UUID profileId,
                                      String doctorRecordNumber,
                                      String notes,
                                      boolean active,
                                      LocalDateTime createdAt,
                                      LocalDateTime updatedAt) {
        return new Doctor(id, profileId, doctorRecordNumber, notes, active, createdAt, updatedAt);
    }

    public DoctorId getId() {
        return id;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public String getDoctorRecordNumber() {
        return doctorRecordNumber;
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
