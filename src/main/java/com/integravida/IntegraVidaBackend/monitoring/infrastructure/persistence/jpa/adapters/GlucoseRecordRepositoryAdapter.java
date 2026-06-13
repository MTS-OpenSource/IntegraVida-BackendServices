package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.GlucoseRecordRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRecord;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.mappers.MonitoringJpaMapper;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.repositories.GlucoseRecordJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GlucoseRecordRepositoryAdapter implements GlucoseRecordRepository {
    private final GlucoseRecordJpaRepository repository;

    public GlucoseRecordRepositoryAdapter(GlucoseRecordJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public GlucoseRecord save(GlucoseRecord glucoseRecord) {
        repository.save(MonitoringJpaMapper.toEntity(glucoseRecord));
        return glucoseRecord;
    }

    @Override
    public Optional<GlucoseRecord> findById(UUID id) {
        return repository.findById(id).map(MonitoringJpaMapper::toDomain);
    }

    @Override
    public List<GlucoseRecord> findByPatientIdAndMeasuredAtBetween(PatientId patientId, LocalDateTime from, LocalDateTime to) {
        return repository.findAllByPatientIdAndMeasuredAtBetweenOrderByMeasuredAtDesc(patientId.value(), from, to)
                .stream().map(MonitoringJpaMapper::toDomain).toList();
    }

    @Override
    public List<GlucoseRecord> findByPatientId(PatientId patientId) {
        return repository.findAllByPatientIdOrderByMeasuredAtDesc(patientId.value()).stream().map(MonitoringJpaMapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
