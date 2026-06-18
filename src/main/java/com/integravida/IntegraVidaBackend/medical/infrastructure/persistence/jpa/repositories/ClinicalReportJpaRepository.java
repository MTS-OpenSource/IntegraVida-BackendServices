package com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.entities.ClinicalReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClinicalReportJpaRepository extends JpaRepository<ClinicalReportEntity, UUID> {
    List<ClinicalReportEntity> findAllByOrderByCreatedAtDesc();

    List<ClinicalReportEntity> findAllByPatientIdOrderByCreatedAtDesc(UUID patientId);
}