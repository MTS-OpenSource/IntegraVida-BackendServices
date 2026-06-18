package com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.entities.DiagnosisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DiagnosisJpaRepository extends JpaRepository<DiagnosisEntity, UUID> {
    List<DiagnosisEntity> findAllByOrderByCreatedAtDesc();

    List<DiagnosisEntity> findAllByPatientIdOrderByCreatedAtDesc(UUID patientId);
}