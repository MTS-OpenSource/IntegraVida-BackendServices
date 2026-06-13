package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.entities.ClinicalObservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClinicalObservationJpaRepository extends JpaRepository<ClinicalObservationEntity, UUID> {
    List<ClinicalObservationEntity> findAllByPatientIdOrderByObservedAtDesc(UUID patientId);
}
