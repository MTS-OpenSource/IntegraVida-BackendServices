package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.mappers;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.Alert;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.ClinicalObservation;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRange;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRecord;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.AlertSeverity;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.entities.AlertEntity;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.entities.ClinicalObservationEntity;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.entities.GlucoseRangeEntity;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.entities.GlucoseRecordEntity;

public final class MonitoringJpaMapper {
    private MonitoringJpaMapper() {
    }

    public static GlucoseRange toDomain(GlucoseRangeEntity entity) {
        return GlucoseRange.reconstitute(
                entity.getId(),
                PatientId.of(entity.getPatientId()),
                GlucoseValue.of(entity.getMinimumValue()),
                GlucoseValue.of(entity.getMaximumValue()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isActive());
    }

    public static GlucoseRangeEntity toEntity(GlucoseRange domain) {
        GlucoseRangeEntity entity = new GlucoseRangeEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId().value());
        entity.setMinimumValue(domain.getMinimumValue().value());
        entity.setMaximumValue(domain.getMaximumValue().value());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setActive(domain.isActive());
        return entity;
    }

    public static GlucoseRecord toDomain(GlucoseRecordEntity entity) {
        return GlucoseRecord.reconstitute(
                entity.getId(),
                PatientId.of(entity.getPatientId()),
                GlucoseValue.of(entity.getGlucoseValue()),
                GlucoseValue.of(entity.getMinimumRange()),
                GlucoseValue.of(entity.getMaximumRange()),
                entity.getMeasuredAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getTriggeredSeverity() != null ? AlertSeverity.valueOf(entity.getTriggeredSeverity()) : null);
    }

    public static GlucoseRecordEntity toEntity(GlucoseRecord domain) {
        GlucoseRecordEntity entity = new GlucoseRecordEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId().value());
        entity.setGlucoseValue(domain.getGlucoseValue().value());
        entity.setMinimumRange(domain.getMinimumRange().value());
        entity.setMaximumRange(domain.getMaximumRange().value());
        entity.setMeasuredAt(domain.getMeasuredAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setTriggeredSeverity(domain.getTriggeredSeverity().map(Enum::name).orElse(null));
        return entity;
    }

    public static Alert toDomain(AlertEntity entity) {
        return Alert.reconstitute(
                entity.getId(),
                entity.getGlucoseRecordId(),
                PatientId.of(entity.getPatientId()),
                GlucoseValue.of(entity.getGlucoseValue()),
                AlertSeverity.valueOf(entity.getSeverity()),
                entity.getMessage(),
                entity.getCreatedAt(),
                entity.getReadAt());
    }

    public static AlertEntity toEntity(Alert domain) {
        AlertEntity entity = new AlertEntity();
        entity.setId(domain.getId());
        entity.setGlucoseRecordId(domain.getGlucoseRecordId());
        entity.setPatientId(domain.getPatientId().value());
        entity.setGlucoseValue(domain.getGlucoseValue().value());
        entity.setSeverity(domain.getSeverity().name());
        entity.setMessage(domain.getMessage());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setReadAt(domain.getReadAt().orElse(null));
        return entity;
    }

    public static ClinicalObservation toDomain(ClinicalObservationEntity entity) {
        return ClinicalObservation.reconstitute(
                entity.getId(),
                PatientId.of(entity.getPatientId()),
                entity.getCategory(),
                entity.getTitle(),
                entity.getContent(),
                entity.getObservedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static ClinicalObservationEntity toEntity(ClinicalObservation domain) {
        ClinicalObservationEntity entity = new ClinicalObservationEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId().value());
        entity.setCategory(domain.getCategory());
        entity.setTitle(domain.getTitle());
        entity.setContent(domain.getContent());
        entity.setObservedAt(domain.getObservedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
