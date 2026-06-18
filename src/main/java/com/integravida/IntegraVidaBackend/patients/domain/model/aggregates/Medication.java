package com.integravida.IntegraVidaBackend.patients.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.MedicationSchedule;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Medication extends AbstractDomainAggregateRoot<Medication> {
    private final UUID id;
    private final PatientId patientId;
    private final UUID treatmentId;
    private String name;
    private String dosage;
    private MedicationSchedule schedule;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime discontinuedAt;

    private Medication(UUID id,
                       PatientId patientId,
                       UUID treatmentId,
                       String name,
                       String dosage,
                       MedicationSchedule schedule,
                       boolean active,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt,
                       LocalDateTime discontinuedAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.treatmentId = Objects.requireNonNull(treatmentId, "treatmentId is required");
        this.name = requireText(name, "name");
        this.dosage = requireText(dosage, "dosage");
        this.schedule = Objects.requireNonNull(schedule, "schedule is required");
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
        this.discontinuedAt = discontinuedAt;
    }

    public static Medication create(UUID id,
                                     PatientId patientId,
                                     UUID treatmentId,
                                     String name,
                                     String dosage,
                                     MedicationSchedule schedule,
                                     LocalDateTime createdAt) {
        return new Medication(id, patientId, treatmentId, name, dosage, schedule, true, createdAt, createdAt, null);
    }

    public static Medication reconstitute(UUID id,
                                          PatientId patientId,
                                          UUID treatmentId,
                                          String name,
                                          String dosage,
                                          MedicationSchedule schedule,
                                          boolean active,
                                          LocalDateTime createdAt,
                                          LocalDateTime updatedAt,
                                          LocalDateTime discontinuedAt) {
        return new Medication(id, patientId, treatmentId, name, dosage, schedule, active, createdAt, updatedAt, discontinuedAt);
    }

    public void update(String name,
                       String dosage,
                       MedicationSchedule schedule,
                       LocalDateTime updatedAt) {
        this.name = requireText(name, "name");
        this.dosage = requireText(dosage, "dosage");
        this.schedule = Objects.requireNonNull(schedule, "schedule is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void discontinue(LocalDateTime discontinuedAt) {
        this.active = false;
        this.discontinuedAt = Objects.requireNonNull(discontinuedAt, "discontinuedAt is required");
        this.updatedAt = discontinuedAt;
    }

    public UUID getId() {
        return id;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public UUID getTreatmentId() {
        return treatmentId;
    }

    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }

    public MedicationSchedule getSchedule() {
        return schedule;
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

    public LocalDateTime getDiscontinuedAt() {
        return discontinuedAt;
    }

    private static String requireText(String value, String field) {
        var text = Objects.requireNonNull(value, field + " is required");
        if (text.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return text;
    }
}
