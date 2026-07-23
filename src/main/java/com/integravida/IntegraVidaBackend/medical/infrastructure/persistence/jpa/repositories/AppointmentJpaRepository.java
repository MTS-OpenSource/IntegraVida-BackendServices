package com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.entities.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, UUID> {
    List<AppointmentEntity> findAllByOrderByScheduledAtDesc();

    List<AppointmentEntity> findAllByPatientIdOrderByScheduledAtDesc(UUID patientId);

    List<AppointmentEntity> findAllByDoctorIdOrderByScheduledAtDesc(UUID doctorId);
}