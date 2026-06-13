package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.entities.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertJpaRepository extends JpaRepository<AlertEntity, UUID> {
    List<AlertEntity> findAllByPatientIdOrderByCreatedAtDesc(UUID patientId);

    List<AlertEntity> findAllByPatientIdAndReadAtIsNullOrderByCreatedAtDesc(UUID patientId);
}
