package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.entities.GlucoseRangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GlucoseRangeJpaRepository extends JpaRepository<GlucoseRangeEntity, UUID> {
    Optional<GlucoseRangeEntity> findFirstByPatientIdAndActiveTrueOrderByUpdatedAtDesc(UUID patientId);

    List<GlucoseRangeEntity> findAllByPatientIdOrderByUpdatedAtDesc(UUID patientId);
}
