package com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.mappers;

import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Appointment;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.AppointmentStatus;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.entities.AppointmentEntity;

import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Diagnosis;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DiagnosisStatus;
import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.entities.DiagnosisEntity;

import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.ClinicalReport;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.ClinicalReportStatus;
import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.entities.ClinicalReportEntity;

public final class MedicalJpaMapper {
    private MedicalJpaMapper() {
    }

    public static Appointment toDomain(AppointmentEntity entity) {
        return Appointment.reconstitute(
                entity.getId(),
                PatientId.of(entity.getPatientId()),
                DoctorId.of(entity.getDoctorId()),
                entity.getScheduledAt(),
                AppointmentStatus.valueOf(entity.getStatus()),
                entity.getReason(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCancelledAt());
    }

    public static AppointmentEntity toEntity(Appointment domain) {
        AppointmentEntity entity = new AppointmentEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId().value());
        entity.setDoctorId(domain.getDoctorId().value());
        entity.setScheduledAt(domain.getScheduledAt());
        entity.setStatus(domain.getStatus().name());
        entity.setReason(domain.getReason());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setCancelledAt(domain.getCancelledAt().orElse(null));
        return entity;
    }

    public static Diagnosis toDomain(DiagnosisEntity entity) {
        return Diagnosis.reconstitute(
                entity.getId(),
                PatientId.of(entity.getPatientId()),
                DoctorId.of(entity.getDoctorId()),
                entity.getDescription(),
                entity.getRecommendation(),
                DiagnosisStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static DiagnosisEntity toEntity(Diagnosis domain) {
        DiagnosisEntity entity = new DiagnosisEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId().value());
        entity.setDoctorId(domain.getDoctorId().value());
        entity.setDescription(domain.getDescription());
        entity.setRecommendation(domain.getRecommendation());
        entity.setStatus(domain.getStatus().name());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public static ClinicalReport toDomain(ClinicalReportEntity entity) {
        return ClinicalReport.reconstitute(
                entity.getId(),
                PatientId.of(entity.getPatientId()),
                DoctorId.of(entity.getDoctorId()),
                entity.getTitle(),
                entity.getSummary(),
                entity.getRecommendations(),
                ClinicalReportStatus.valueOf(entity.getStatus()),
                entity.getIssuedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static ClinicalReportEntity toEntity(ClinicalReport domain) {
        ClinicalReportEntity entity = new ClinicalReportEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId().value());
        entity.setDoctorId(domain.getDoctorId().value());
        entity.setTitle(domain.getTitle());
        entity.setSummary(domain.getSummary());
        entity.setRecommendations(domain.getRecommendations());
        entity.setStatus(domain.getStatus().name());
        entity.setIssuedAt(domain.getIssuedAt().orElse(null));
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}