package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "glucose_records")
public class GlucoseRecordEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal glucoseValue;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal minimumRange;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal maximumRange;

    @Column(nullable = false)
    private LocalDateTime measuredAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private String triggeredSeverity;

    public GlucoseRecordEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public BigDecimal getGlucoseValue() {
        return glucoseValue;
    }

    public void setGlucoseValue(BigDecimal glucoseValue) {
        this.glucoseValue = glucoseValue;
    }

    public BigDecimal getMinimumRange() {
        return minimumRange;
    }

    public void setMinimumRange(BigDecimal minimumRange) {
        this.minimumRange = minimumRange;
    }

    public BigDecimal getMaximumRange() {
        return maximumRange;
    }

    public void setMaximumRange(BigDecimal maximumRange) {
        this.maximumRange = maximumRange;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public void setMeasuredAt(LocalDateTime measuredAt) {
        this.measuredAt = measuredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTriggeredSeverity() {
        return triggeredSeverity;
    }

    public void setTriggeredSeverity(String triggeredSeverity) {
        this.triggeredSeverity = triggeredSeverity;
    }
}
