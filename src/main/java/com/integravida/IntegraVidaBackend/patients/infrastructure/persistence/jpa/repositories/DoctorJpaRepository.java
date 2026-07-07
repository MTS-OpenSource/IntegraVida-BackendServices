package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, UUID> {
    Optional<DoctorEntity> findByProfileId(UUID profileId);

    boolean existsByProfileId(UUID profileId);

    boolean existsByDoctorRecordNumber(String doctorRecordNumber);
}
