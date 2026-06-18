package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, UUID> {
    Optional<PatientEntity> findByProfileId(UUID profileId);

    boolean existsByProfileId(UUID profileId);

    boolean existsByMedicalRecordNumber(String medicalRecordNumber);
}
