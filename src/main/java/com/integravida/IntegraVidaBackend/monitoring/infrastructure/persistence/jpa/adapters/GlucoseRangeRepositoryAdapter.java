package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.GlucoseRangeRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRange;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.mappers.MonitoringJpaMapper;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.repositories.GlucoseRangeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GlucoseRangeRepositoryAdapter implements GlucoseRangeRepository {
    private final GlucoseRangeJpaRepository repository;

    public GlucoseRangeRepositoryAdapter(GlucoseRangeJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public GlucoseRange save(GlucoseRange glucoseRange) {
        repository.save(MonitoringJpaMapper.toEntity(glucoseRange));
        return glucoseRange;
    }

    @Override
    public Optional<GlucoseRange> findById(UUID id) {
        return repository.findById(id).map(MonitoringJpaMapper::toDomain);
    }

    @Override
    public Optional<GlucoseRange> findActiveByPatientId(PatientId patientId) {
        return repository.findFirstByPatientIdAndActiveTrueOrderByUpdatedAtDesc(patientId.value()).map(MonitoringJpaMapper::toDomain);
    }

    @Override
    public List<GlucoseRange> findByPatientId(PatientId patientId) {
        return repository.findAllByPatientIdOrderByUpdatedAtDesc(patientId.value()).stream().map(MonitoringJpaMapper::toDomain).toList();
    }
}
