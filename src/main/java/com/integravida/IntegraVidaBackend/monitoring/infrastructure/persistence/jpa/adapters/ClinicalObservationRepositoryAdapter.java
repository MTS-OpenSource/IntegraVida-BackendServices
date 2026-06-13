package com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.ClinicalObservationRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.ClinicalObservation;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.mappers.MonitoringJpaMapper;
import com.integravida.IntegraVidaBackend.monitoring.infrastructure.persistence.jpa.repositories.ClinicalObservationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ClinicalObservationRepositoryAdapter implements ClinicalObservationRepository {
    private final ClinicalObservationJpaRepository repository;

    public ClinicalObservationRepositoryAdapter(ClinicalObservationJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClinicalObservation save(ClinicalObservation clinicalObservation) {
        repository.save(MonitoringJpaMapper.toEntity(clinicalObservation));
        return clinicalObservation;
    }

    @Override
    public Optional<ClinicalObservation> findById(UUID id) {
        return repository.findById(id).map(MonitoringJpaMapper::toDomain);
    }

    @Override
    public List<ClinicalObservation> findByPatientId(PatientId patientId) {
        return repository.findAllByPatientIdOrderByObservedAtDesc(patientId.value()).stream().map(MonitoringJpaMapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
