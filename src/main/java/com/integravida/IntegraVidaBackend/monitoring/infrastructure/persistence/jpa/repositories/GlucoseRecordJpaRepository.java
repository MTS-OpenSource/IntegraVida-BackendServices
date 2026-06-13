package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.entities.GlucoseRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GlucoseRecordJpaRepository extends JpaRepository<GlucoseRecordEntity, UUID> {
    List<GlucoseRecordEntity> findAllByPatientIdOrderByMeasuredAtDesc(UUID patientId);

    List<GlucoseRecordEntity> findAllByPatientIdAndMeasuredAtBetweenOrderByMeasuredAtDesc(UUID patientId,
                                                                                        LocalDateTime from,
                                                                                        LocalDateTime to);
}
