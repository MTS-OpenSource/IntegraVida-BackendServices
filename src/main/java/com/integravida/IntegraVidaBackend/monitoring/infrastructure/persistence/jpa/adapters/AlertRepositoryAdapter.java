package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.AlertRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.Alert;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.mappers.MonitoringJpaMapper;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.repositories.AlertJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AlertRepositoryAdapter implements AlertRepository {
    private final AlertJpaRepository repository;

    public AlertRepositoryAdapter(AlertJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Alert save(Alert alert) {
        repository.save(MonitoringJpaMapper.toEntity(alert));
        return alert;
    }

    @Override
    public Optional<Alert> findById(UUID id) {
        return repository.findById(id).map(MonitoringJpaMapper::toDomain);
    }

    @Override
    public List<Alert> findByPatientId(PatientId patientId) {
        return repository.findAllByPatientIdOrderByCreatedAtDesc(patientId.value()).stream().map(MonitoringJpaMapper::toDomain).toList();
    }

    @Override
    public List<Alert> findUnreadByPatientId(PatientId patientId) {
        return repository.findAllByPatientIdAndReadAtIsNullOrderByCreatedAtDesc(patientId.value()).stream().map(MonitoringJpaMapper::toDomain).toList();
    }
}
