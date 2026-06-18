package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.MedicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicationJpaRepository extends JpaRepository<MedicationEntity, UUID> {
    List<MedicationEntity> findAllByOrderByCreatedAtDesc();

    List<MedicationEntity> findAllByPatientIdOrderByCreatedAtDesc(UUID patientId);

    List<MedicationEntity> findAllByTreatmentIdOrderByCreatedAtDesc(UUID treatmentId);
}
