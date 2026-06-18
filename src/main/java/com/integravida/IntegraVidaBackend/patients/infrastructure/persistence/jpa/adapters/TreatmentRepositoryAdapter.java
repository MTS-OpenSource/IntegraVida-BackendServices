package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.TreatmentRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Treatment;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.mappers.PatientsJpaMapper;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories.TreatmentJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TreatmentRepositoryAdapter implements TreatmentRepository {
    private final TreatmentJpaRepository jpaRepository;

    public TreatmentRepositoryAdapter(TreatmentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Treatment save(Treatment treatment) {
        return PatientsJpaMapper.toDomain(jpaRepository.save(PatientsJpaMapper.toEntity(treatment)));
    }

    @Override
    public Optional<Treatment> findById(UUID id) {
        return jpaRepository.findById(id).map(PatientsJpaMapper::toDomain);
    }

    @Override
    public List<Treatment> findAll() {
        return jpaRepository.findAllByOrderByCreatedAtDesc().stream().map(PatientsJpaMapper::toDomain).toList();
    }

    @Override
    public List<Treatment> findByPatientId(PatientId patientId) {
        return jpaRepository.findAllByPatientIdOrderByCreatedAtDesc(patientId.value()).stream().map(PatientsJpaMapper::toDomain).toList();
    }

    @Override
    public Optional<Treatment> findActiveByPatientId(PatientId patientId) {
        return jpaRepository.findFirstByPatientIdAndStatusOrderByCreatedAtDesc(patientId.value(), "ACTIVE")
                .map(PatientsJpaMapper::toDomain);
    }
}
