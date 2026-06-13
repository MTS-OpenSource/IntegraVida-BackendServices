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

public final class Alert extends AbstractDomainAggregateRoot<Alert> {
    private final UUID id;
    private final UUID glucoseRecordId;
    private final PatientId patientId;
    private final GlucoseValue glucoseValue;
    private final AlertSeverity severity;
    private final String message;
    private final LocalDateTime createdAt;
    private LocalDateTime readAt;

    private Alert(UUID id,
                  UUID glucoseRecordId,
                  PatientId patientId,
                  GlucoseValue glucoseValue,
                  AlertSeverity severity,
                  String message,
                  LocalDateTime createdAt,
                  LocalDateTime readAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.glucoseRecordId = Objects.requireNonNull(glucoseRecordId, "glucoseRecordId is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.glucoseValue = Objects.requireNonNull(glucoseValue, "glucoseValue is required");
        this.severity = Objects.requireNonNull(severity, "severity is required");
        this.message = Objects.requireNonNull(message, "message is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.readAt = readAt;
    }

    public static Alert fromGlucoseAlertTriggeredEvent(UUID id, GlucoseAlertTriggeredEvent event) {
        return new Alert(
                id,
                event.glucoseRecordId(),
                event.patientId(),
                event.glucoseValue(),
                event.severity(),
                event.message(),
                event.measuredAt(),
                null);
    }

    public static Alert reconstitute(UUID id,
                                     UUID glucoseRecordId,
                                     PatientId patientId,
                                     GlucoseValue glucoseValue,
                                     AlertSeverity severity,
                                     String message,
                                     LocalDateTime createdAt,
                                     LocalDateTime readAt) {
        return new Alert(id, glucoseRecordId, patientId, glucoseValue, severity, message, createdAt, readAt);
    }

    public void markAsRead(LocalDateTime readAt) {
        this.readAt = Objects.requireNonNull(readAt, "readAt is required");
    }

    public UUID getId() {
        return id;
    }

    public UUID getGlucoseRecordId() {
        return glucoseRecordId;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public GlucoseValue getGlucoseValue() {
        return glucoseValue;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Optional<LocalDateTime> getReadAt() {
        return Optional.ofNullable(readAt);
    }

    public boolean isRead() {
        return readAt != null;
    }
}
