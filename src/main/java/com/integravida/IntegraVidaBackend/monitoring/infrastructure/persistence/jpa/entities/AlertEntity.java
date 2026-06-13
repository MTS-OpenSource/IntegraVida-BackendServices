package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alerts")
public class AlertEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID glucoseRecordId;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal glucoseValue;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime readAt;

    public AlertEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGlucoseRecordId() {
        return glucoseRecordId;
    }

    public void setGlucoseRecordId(UUID glucoseRecordId) {
        this.glucoseRecordId = glucoseRecordId;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}
