package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.PatientDoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientDoctorJpaRepository extends JpaRepository<PatientDoctorEntity, UUID> {
    List<PatientDoctorEntity> findByDoctorId(UUID doctorId);

    Optional<PatientDoctorEntity> findByPatientId(UUID patientId);

    boolean existsByPatientIdAndDoctorId(UUID patientId, UUID doctorId);
}
