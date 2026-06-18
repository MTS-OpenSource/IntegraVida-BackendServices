package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.mappers.PatientsJpaMapper;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories.PatientJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PatientRepositoryAdapter implements PatientRepository {
    private final PatientJpaRepository jpaRepository;

    public PatientRepositoryAdapter(PatientJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Patient save(Patient patient) {
        return PatientsJpaMapper.toDomain(jpaRepository.save(PatientsJpaMapper.toEntity(patient)));
    }

    @Override
    public Optional<Patient> findById(PatientId id) {
        return jpaRepository.findById(id.value()).map(PatientsJpaMapper::toDomain);
    }

    @Override
    public Optional<Patient> findByProfileId(UUID profileId) {
        return jpaRepository.findByProfileId(profileId).map(PatientsJpaMapper::toDomain);
    }

    @Override
    public List<Patient> findAll() {
        return jpaRepository.findAll().stream().map(PatientsJpaMapper::toDomain).toList();
    }

    @Override
    public boolean existsByProfileId(UUID profileId) {
        return jpaRepository.existsByProfileId(profileId);
    }

    @Override
    public boolean existsByMedicalRecordNumber(String medicalRecordNumber) {
        return jpaRepository.existsByMedicalRecordNumber(medicalRecordNumber);
    }
}
