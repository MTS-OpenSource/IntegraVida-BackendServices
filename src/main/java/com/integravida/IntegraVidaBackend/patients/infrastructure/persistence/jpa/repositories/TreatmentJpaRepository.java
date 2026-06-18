package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreatmentJpaRepository extends JpaRepository<TreatmentEntity, UUID> {
    List<TreatmentEntity> findAllByOrderByCreatedAtDesc();

    List<TreatmentEntity> findAllByPatientIdOrderByCreatedAtDesc(UUID patientId);

    Optional<TreatmentEntity> findFirstByPatientIdAndStatusOrderByCreatedAtDesc(UUID patientId, String status);
}
