package com.integravida.IntegraVidaBackend.patients.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.TreatmentStatus;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Treatment extends AbstractDomainAggregateRoot<Treatment> {
    private final UUID id;
    private final PatientId patientId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private TreatmentStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Treatment(UUID id,
                      PatientId patientId,
                      String name,
                      String description,
                      LocalDate startDate,
                      LocalDate endDate,
                      TreatmentStatus status,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.name = requireText(name, "name");
        this.description = requireText(description, "description");
        this.startDate = Objects.requireNonNull(startDate, "startDate is required");
        this.endDate = endDate;
        this.status = Objects.requireNonNull(status, "status is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");

        validateDates();
    }

    public static Treatment create(UUID id,
                                   PatientId patientId,
                                   String name,
                                   String description,
                                   LocalDate startDate,
                                   LocalDate endDate,
                                   LocalDateTime createdAt) {
        return new Treatment(id, patientId, name, description, startDate, endDate, TreatmentStatus.PLANNED, createdAt, createdAt);
    }

    public static Treatment reconstitute(UUID id,
                                         PatientId patientId,
                                         String name,
                                         String description,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         TreatmentStatus status,
                                         LocalDateTime createdAt,
                                         LocalDateTime updatedAt) {
        return new Treatment(id, patientId, name, description, startDate, endDate, status, createdAt, updatedAt);
    }

    public void update(String name,
                       String description,
                       LocalDate startDate,
                       LocalDate endDate,
                       LocalDateTime updatedAt) {
        this.name = requireText(name, "name");
        this.description = requireText(description, "description");
        this.startDate = Objects.requireNonNull(startDate, "startDate is required");
        this.endDate = endDate;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
        validateDates();
    }

    public void activate(LocalDateTime updatedAt) {
        ensureNotFinished("activate");
        this.status = TreatmentStatus.ACTIVE;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void pause(LocalDateTime updatedAt) {
        ensureCanTransition();
        this.status = TreatmentStatus.PAUSED;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void complete(LocalDateTime updatedAt) {
        ensureCanTransition();
        this.status = TreatmentStatus.COMPLETED;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void cancel(LocalDateTime updatedAt) {
        ensureCanTransition();
        this.status = TreatmentStatus.CANCELLED;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public UUID getId() {
        return id;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public TreatmentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private void validateDates() {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate cannot be before startDate");
        }
    }

    private void ensureCanTransition() {
        if (status == TreatmentStatus.COMPLETED || status == TreatmentStatus.CANCELLED) {
            throw new IllegalStateException("finished treatments cannot be modified");
        }
    }

    private void ensureNotFinished(String operation) {
        if (status == TreatmentStatus.COMPLETED || status == TreatmentStatus.CANCELLED) {
            throw new IllegalStateException("finished treatments cannot be " + operation);
        }
    }

    private static String requireText(String value, String field) {
        var text = Objects.requireNonNull(value, field + " is required");
        if (text.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return text;
    }
}
