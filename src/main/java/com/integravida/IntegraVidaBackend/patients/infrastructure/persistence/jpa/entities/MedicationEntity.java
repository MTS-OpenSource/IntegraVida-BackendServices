package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medication_entity")
public class MedicationEntity {
    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID patientId;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID treatmentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String dosage;

    @Lob
    @Column(nullable = false)
    private String daysOfWeek;

    @Lob
    @Column(nullable = false)
    private String doseTimes;

    @Lob
    @Column(nullable = false)
    private String instructions;

    @Column(nullable = false)
    private boolean active;

    private LocalDateTime discontinuedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

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

    public UUID getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(UUID treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getDoseTimes() {
        return doseTimes;
    }

    public void setDoseTimes(String doseTimes) {
        this.doseTimes = doseTimes;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getDiscontinuedAt() {
        return discontinuedAt;
    }

    public void setDiscontinuedAt(LocalDateTime discontinuedAt) {
        this.discontinuedAt = discontinuedAt;
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
}
