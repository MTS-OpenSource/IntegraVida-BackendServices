package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.MedicationIntakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicationIntakeJpaRepository extends JpaRepository<MedicationIntakeEntity, UUID> {
    List<MedicationIntakeEntity> findAllByPatientIdOrderByCreatedAtDesc(UUID patientId);

    List<MedicationIntakeEntity> findAllByMedicationIdOrderByCreatedAtDesc(UUID medicationId);
}
