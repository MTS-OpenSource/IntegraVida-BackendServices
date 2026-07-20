package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientDoctorRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.PatientDoctor;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.mappers.PatientsJpaMapper;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories.PatientDoctorJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PatientDoctorRepositoryAdapter implements PatientDoctorRepository {
    private final PatientDoctorJpaRepository jpaRepository;

    public PatientDoctorRepositoryAdapter(PatientDoctorJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PatientDoctor save(PatientDoctor patientDoctor) {
        return PatientsJpaMapper.toDomain(jpaRepository.save(PatientsJpaMapper.toEntity(patientDoctor)));
    }

    @Override
    public List<PatientDoctor> findAll() {
        return jpaRepository.findAll().stream().map(PatientsJpaMapper::toDomain).toList();
    }

    @Override
    public Optional<PatientDoctor> findById(UUID id) {
        return jpaRepository.findById(id).map(PatientsJpaMapper::toDomain);
    }

    @Override
    public List<PatientDoctor> findByDoctorId(UUID doctorId) {
        return jpaRepository.findByDoctorId(doctorId).stream().map(PatientsJpaMapper::toDomain).toList();
    }

    @Override
    public Optional<PatientDoctor> findByPatientId(UUID patientId) {
        return jpaRepository.findByPatientId(patientId).map(PatientsJpaMapper::toDomain);
    }

    @Override
    public boolean existsByPatientIdAndDoctorId(UUID patientId, UUID doctorId) {
        return jpaRepository.existsByPatientIdAndDoctorId(patientId, doctorId);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
