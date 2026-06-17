package com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.mappers;

import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Appointment;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.AppointmentStatus;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.entities.AppointmentEntity;

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
}