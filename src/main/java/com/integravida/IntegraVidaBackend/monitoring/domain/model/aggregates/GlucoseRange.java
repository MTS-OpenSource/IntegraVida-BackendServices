package com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.AlertSeverity;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class GlucoseRange extends AbstractDomainAggregateRoot<GlucoseRange> {
    private final UUID id;
    private final PatientId patientId;
    private GlucoseValue minimumValue;
    private GlucoseValue maximumValue;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;

    private GlucoseRange(UUID id,
                         PatientId patientId,
                         GlucoseValue minimumValue,
                         GlucoseValue maximumValue,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         boolean active) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.minimumValue = Objects.requireNonNull(minimumValue, "minimumValue is required");
        this.maximumValue = Objects.requireNonNull(maximumValue, "maximumValue is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
        this.active = active;
        ensureValidRange();
    }

    public static GlucoseRange create(UUID id,
                                      PatientId patientId,
                                      GlucoseValue minimumValue,
                                      GlucoseValue maximumValue,
                                      LocalDateTime now) {
        return new GlucoseRange(id, patientId, minimumValue, maximumValue, now, now, true);
    }

    public static GlucoseRange reconstitute(UUID id,
                                            PatientId patientId,
                                            GlucoseValue minimumValue,
                                            GlucoseValue maximumValue,
                                            LocalDateTime createdAt,
                                            LocalDateTime updatedAt,
                                            boolean active) {
        return new GlucoseRange(id, patientId, minimumValue, maximumValue, createdAt, updatedAt, active);
    }

    public void updateRange(GlucoseValue minimumValue, GlucoseValue maximumValue, LocalDateTime now) {
        this.minimumValue = Objects.requireNonNull(minimumValue, "minimumValue is required");
        this.maximumValue = Objects.requireNonNull(maximumValue, "maximumValue is required");
        this.updatedAt = Objects.requireNonNull(now, "updatedAt is required");
        ensureValidRange();
    }

    public void deactivate(LocalDateTime now) {
        this.active = false;
        this.updatedAt = Objects.requireNonNull(now, "updatedAt is required");
    }

    public boolean contains(GlucoseValue value) {
        return !value.isBelow(minimumValue.value()) && !value.isAbove(maximumValue.value());
    }

    public Optional<AlertSeverity> classify(GlucoseValue value) {
        if (contains(value)) {
            return Optional.empty();
        }

        BigDecimal deviation;
        if (value.isBelow(minimumValue.value())) {
            deviation = minimumValue.value().subtract(value.value())
                    .divide(minimumValue.value().max(BigDecimal.ONE), 4, RoundingMode.HALF_UP);
        } else {
            deviation = value.value().subtract(maximumValue.value())
                    .divide(maximumValue.value().max(BigDecimal.ONE), 4, RoundingMode.HALF_UP);
        }
        return Optional.of(AlertSeverity.fromDeviationRatio(deviation.abs()));
    }

    private void ensureValidRange() {
        if (minimumValue.value().compareTo(maximumValue.value()) >= 0) {
            throw new IllegalArgumentException("minimum glucose value must be lower than maximum glucose value");
        }
    }

    public UUID getId() {
        return id;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public GlucoseValue getMinimumValue() {
        return minimumValue;
    }

    public GlucoseValue getMaximumValue() {
        return maximumValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }
}
