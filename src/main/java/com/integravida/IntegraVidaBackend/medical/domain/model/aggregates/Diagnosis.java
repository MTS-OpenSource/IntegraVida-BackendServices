package com.integravida.IntegraVidaBackend.medical.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DiagnosisStatus;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Diagnosis extends AbstractDomainAggregateRoot<Diagnosis> {
    private final UUID id;
    private final PatientId patientId;
    private final DoctorId doctorId;
    private String description;
    private String recommendation;
    private DiagnosisStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Diagnosis(UUID id,
                      PatientId patientId,
                      DoctorId doctorId,
                      String description,
                      String recommendation,
                      DiagnosisStatus status,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.doctorId = Objects.requireNonNull(doctorId, "doctorId is required");
        this.description = Objects.requireNonNull(description, "description is required");
        this.recommendation = Objects.requireNonNull(recommendation, "recommendation is required");
        this.status = Objects.requireNonNull(status, "status is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public static Diagnosis create(UUID id,
                                   PatientId patientId,
                                   DoctorId doctorId,
                                   String description,
                                   String recommendation,
                                   LocalDateTime createdAt) {
        return new Diagnosis(
                id,
                patientId,
                doctorId,
                description,
                recommendation,
                DiagnosisStatus.DRAFT,
                createdAt,
                createdAt);
    }

    public static Diagnosis reconstitute(UUID id,
                                         PatientId patientId,
                                         DoctorId doctorId,
                                         String description,
                                         String recommendation,
                                         DiagnosisStatus status,
                                         LocalDateTime createdAt,
                                         LocalDateTime updatedAt) {
        return new Diagnosis(
                id,
                patientId,
                doctorId,
                description,
                recommendation,
                status,
                createdAt,
                updatedAt);
    }

    public void update(String description, String recommendation, LocalDateTime updatedAt) {
        if (this.status == DiagnosisStatus.ARCHIVED) {
            throw new IllegalStateException("archived diagnoses cannot be updated");
        }

        this.description = Objects.requireNonNull(description, "description is required");
        this.recommendation = Objects.requireNonNull(recommendation, "recommendation is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void confirm(LocalDateTime updatedAt) {
        if (this.status == DiagnosisStatus.ARCHIVED) {
            throw new IllegalStateException("archived diagnoses cannot be confirmed");
        }

        this.status = DiagnosisStatus.CONFIRMED;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void archive(LocalDateTime updatedAt) {
        this.status = DiagnosisStatus.ARCHIVED;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public UUID getId() {
        return id;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public DoctorId getDoctorId() {
        return doctorId;
    }

    public String getDescription() {
        return description;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public DiagnosisStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}