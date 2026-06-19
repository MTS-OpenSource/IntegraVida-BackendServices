package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medication_intake_entity")
public class MedicationIntakeEntity {
    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID medicationId;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private LocalDateTime takenAt;

    @Column(nullable = false, columnDefinition = "text")
    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(UUID medicationId) {
        this.medicationId = medicationId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
