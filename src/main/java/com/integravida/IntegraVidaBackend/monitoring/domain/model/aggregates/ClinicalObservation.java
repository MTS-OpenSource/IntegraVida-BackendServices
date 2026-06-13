package com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class ClinicalObservation extends AbstractDomainAggregateRoot<ClinicalObservation> {
    private final UUID id;
    private final PatientId patientId;
    private String category;
    private String title;
    private String content;
    private LocalDateTime observedAt;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ClinicalObservation(UUID id,
                                PatientId patientId,
                                String category,
                                String title,
                                String content,
                                LocalDateTime observedAt,
                                LocalDateTime createdAt,
                                LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.category = Objects.requireNonNull(category, "category is required");
        this.title = Objects.requireNonNull(title, "title is required");
        this.content = Objects.requireNonNull(content, "content is required");
        this.observedAt = Objects.requireNonNull(observedAt, "observedAt is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public static ClinicalObservation create(UUID id,
                                             PatientId patientId,
                                             String category,
                                             String title,
                                             String content,
                                             LocalDateTime observedAt,
                                             LocalDateTime now) {
        return new ClinicalObservation(id, patientId, category, title, content, observedAt, now, now);
    }

    public static ClinicalObservation reconstitute(UUID id,
                                                   PatientId patientId,
                                                   String category,
                                                   String title,
                                                   String content,
                                                   LocalDateTime observedAt,
                                                   LocalDateTime createdAt,
                                                   LocalDateTime updatedAt) {
        return new ClinicalObservation(id, patientId, category, title, content, observedAt, createdAt, updatedAt);
    }

    public void update(String category, String title, String content, LocalDateTime observedAt, LocalDateTime now) {
        this.category = Objects.requireNonNull(category, "category is required");
        this.title = Objects.requireNonNull(title, "title is required");
        this.content = Objects.requireNonNull(content, "content is required");
        this.observedAt = Objects.requireNonNull(observedAt, "observedAt is required");
        this.updatedAt = Objects.requireNonNull(now, "updatedAt is required");
    }

    public UUID getId() {
        return id;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getObservedAt() {
        return observedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
