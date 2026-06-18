package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.MedicationRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Medication;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.mappers.PatientsJpaMapper;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories.MedicationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MedicationRepositoryAdapter implements MedicationRepository {
    private final MedicationJpaRepository jpaRepository;

    public MedicationRepositoryAdapter(MedicationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Medication save(Medication medication) {
        return PatientsJpaMapper.toDomain(jpaRepository.save(PatientsJpaMapper.toEntity(medication)));
    }

    @Override
    public Optional<Medication> findById(UUID id) {
        return jpaRepository.findById(id).map(PatientsJpaMapper::toDomain);
    }

    @Override
    public List<Medication> findAll() {
        return jpaRepository.findAllByOrderByCreatedAtDesc().stream().map(PatientsJpaMapper::toDomain).toList();
    }

    @Override
    public List<Medication> findByPatientId(PatientId patientId) {
        return jpaRepository.findAllByPatientIdOrderByCreatedAtDesc(patientId.value()).stream().map(PatientsJpaMapper::toDomain).toList();
    }

    @Override
    public List<Medication> findByTreatmentId(UUID treatmentId) {
        return jpaRepository.findAllByTreatmentIdOrderByCreatedAtDesc(treatmentId).stream().map(PatientsJpaMapper::toDomain).toList();
    }
}
