package com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.events.GlucoseAlertTriggeredEvent;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.AlertSeverity;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class GlucoseRecord extends AbstractDomainAggregateRoot<GlucoseRecord> {
    private final UUID id;
    private final PatientId patientId;
    private GlucoseValue glucoseValue;
    private GlucoseValue minimumRange;
    private GlucoseValue maximumRange;
    private LocalDateTime measuredAt;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AlertSeverity triggeredSeverity;

    private GlucoseRecord(UUID id,
                          PatientId patientId,
                          GlucoseValue glucoseValue,
                          GlucoseValue minimumRange,
                          GlucoseValue maximumRange,
                          LocalDateTime measuredAt,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt,
                          AlertSeverity triggeredSeverity) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.glucoseValue = Objects.requireNonNull(glucoseValue, "glucoseValue is required");
        this.minimumRange = Objects.requireNonNull(minimumRange, "minimumRange is required");
        this.maximumRange = Objects.requireNonNull(maximumRange, "maximumRange is required");
        this.measuredAt = Objects.requireNonNull(measuredAt, "measuredAt is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
        this.triggeredSeverity = triggeredSeverity;
        ensureValidRange();
    }

    public static GlucoseRecord create(UUID id,
                                       PatientId patientId,
                                       GlucoseValue glucoseValue,
                                       GlucoseRange glucoseRange,
                                       LocalDateTime measuredAt,
                                       LocalDateTime createdAt) {
        var severity = glucoseRange.classify(glucoseValue).orElse(null);
        var record = new GlucoseRecord(
                id,
                patientId,
                glucoseValue,
                glucoseRange.getMinimumValue(),
                glucoseRange.getMaximumValue(),
                measuredAt,
                createdAt,
                createdAt,
                severity);
        record.registerAlertIfNeeded(severity);
        return record;
    }

    public static GlucoseRecord reconstitute(UUID id,
                                             PatientId patientId,
                                             GlucoseValue glucoseValue,
                                             GlucoseValue minimumRange,
                                             GlucoseValue maximumRange,
                                             LocalDateTime measuredAt,
                                             LocalDateTime createdAt,
                                             LocalDateTime updatedAt,
                                             AlertSeverity triggeredSeverity) {
        return new GlucoseRecord(id, patientId, glucoseValue, minimumRange, maximumRange, measuredAt, createdAt, updatedAt, triggeredSeverity);
    }

    public void update(GlucoseValue glucoseValue,
                       GlucoseRange glucoseRange,
                       LocalDateTime measuredAt,
                       LocalDateTime updatedAt) {
        this.glucoseValue = Objects.requireNonNull(glucoseValue, "glucoseValue is required");
        this.minimumRange = Objects.requireNonNull(glucoseRange.getMinimumValue(), "minimumRange is required");
        this.maximumRange = Objects.requireNonNull(glucoseRange.getMaximumValue(), "maximumRange is required");
        this.measuredAt = Objects.requireNonNull(measuredAt, "measuredAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
        ensureValidRange();

        var severity = glucoseRange.classify(glucoseValue).orElse(null);
        this.triggeredSeverity = severity;
        if (severity != null) {
            registerAlertIfNeeded(severity);
        }
    }

    private void registerAlertIfNeeded(AlertSeverity severity) {
        if (severity != null) {
            registerDomainEvent(new GlucoseAlertTriggeredEvent(
                    id,
                    patientId,
                    glucoseValue,
                    minimumRange,
                    maximumRange,
                    severity,
                    measuredAt));
        }
    }

    private void ensureValidRange() {
        if (minimumRange.value().compareTo(maximumRange.value()) >= 0) {
            throw new IllegalArgumentException("minimum glucose range must be lower than maximum glucose range");
        }
    }

    public UUID getId() {
        return id;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public GlucoseValue getGlucoseValue() {
        return glucoseValue;
    }

    public GlucoseValue getMinimumRange() {
        return minimumRange;
    }

    public GlucoseValue getMaximumRange() {
        return maximumRange;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Optional<AlertSeverity> getTriggeredSeverity() {
        return Optional.ofNullable(triggeredSeverity);
    }
}
