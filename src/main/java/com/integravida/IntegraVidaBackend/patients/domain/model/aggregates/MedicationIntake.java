package com.integravida.IntegraVidaBackend.patients.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class MedicationIntake extends AbstractDomainAggregateRoot<MedicationIntake> {
    private final UUID id;
    private final UUID medicationId;
    private final PatientId patientId;
    private final LocalDateTime takenAt;
    private final String notes;
    private final LocalDateTime createdAt;

    private MedicationIntake(UUID id,
                             UUID medicationId,
                             PatientId patientId,
                             LocalDateTime takenAt,
                             String notes,
                             LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.medicationId = Objects.requireNonNull(medicationId, "medicationId is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.takenAt = Objects.requireNonNull(takenAt, "takenAt is required");
        this.notes = requireText(notes, "notes");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
    }

    public static MedicationIntake create(UUID id,
                                          UUID medicationId,
                                          PatientId patientId,
                                          LocalDateTime takenAt,
                                          String notes,
                                          LocalDateTime createdAt) {
        return new MedicationIntake(id, medicationId, patientId, takenAt, notes, createdAt);
    }

    public static MedicationIntake reconstitute(UUID id,
                                                UUID medicationId,
                                                PatientId patientId,
                                                LocalDateTime takenAt,
                                                String notes,
                                                LocalDateTime createdAt) {
        return new MedicationIntake(id, medicationId, patientId, takenAt, notes, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getMedicationId() {
        return medicationId;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    private static String requireText(String value, String field) {
        var text = Objects.requireNonNull(value, field + " is required");
        if (text.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return text;
    }
}
