package com.integravida.IntegraVidaBackend.medical.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.ClinicalReportStatus;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class ClinicalReport extends AbstractDomainAggregateRoot<ClinicalReport> {
    private final UUID id;
    private final PatientId patientId;
    private final DoctorId doctorId;
    private String title;
    private String summary;
    private String recommendations;
    private ClinicalReportStatus status;
    private LocalDateTime issuedAt;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ClinicalReport(UUID id,
                           PatientId patientId,
                           DoctorId doctorId,
                           String title,
                           String summary,
                           String recommendations,
                           ClinicalReportStatus status,
                           LocalDateTime issuedAt,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.doctorId = Objects.requireNonNull(doctorId, "doctorId is required");
        this.title = Objects.requireNonNull(title, "title is required");
        this.summary = Objects.requireNonNull(summary, "summary is required");
        this.recommendations = Objects.requireNonNull(recommendations, "recommendations is required");
        this.status = Objects.requireNonNull(status, "status is required");
        this.issuedAt = issuedAt;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public static ClinicalReport create(UUID id,
                                        PatientId patientId,
                                        DoctorId doctorId,
                                        String title,
                                        String summary,
                                        String recommendations,
                                        LocalDateTime createdAt) {
        return new ClinicalReport(
                id,
                patientId,
                doctorId,
                title,
                summary,
                recommendations,
                ClinicalReportStatus.DRAFT,
                null,
                createdAt,
                createdAt);
    }

    public static ClinicalReport reconstitute(UUID id,
                                              PatientId patientId,
                                              DoctorId doctorId,
                                              String title,
                                              String summary,
                                              String recommendations,
                                              ClinicalReportStatus status,
                                              LocalDateTime issuedAt,
                                              LocalDateTime createdAt,
                                              LocalDateTime updatedAt) {
        return new ClinicalReport(
                id,
                patientId,
                doctorId,
                title,
                summary,
                recommendations,
                status,
                issuedAt,
                createdAt,
                updatedAt);
    }

    public void update(String title,
                       String summary,
                       String recommendations,
                       LocalDateTime updatedAt) {
        if (this.status == ClinicalReportStatus.ARCHIVED) {
            throw new IllegalStateException("archived clinical reports cannot be updated");
        }

        this.title = Objects.requireNonNull(title, "title is required");
        this.summary = Objects.requireNonNull(summary, "summary is required");
        this.recommendations = Objects.requireNonNull(recommendations, "recommendations is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void issue(LocalDateTime issuedAt) {
        if (this.status == ClinicalReportStatus.ARCHIVED) {
            throw new IllegalStateException("archived clinical reports cannot be issued");
        }

        this.status = ClinicalReportStatus.ISSUED;
        this.issuedAt = Objects.requireNonNull(issuedAt, "issuedAt is required");
        this.updatedAt = issuedAt;
    }

    public void archive(LocalDateTime updatedAt) {
        this.status = ClinicalReportStatus.ARCHIVED;
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

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public ClinicalReportStatus getStatus() {
        return status;
    }

    public Optional<LocalDateTime> getIssuedAt() {
        return Optional.ofNullable(issuedAt);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}