package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.DoctorRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Doctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.mappers.PatientsJpaMapper;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories.DoctorJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DoctorRepositoryAdapter implements DoctorRepository {
    private final DoctorJpaRepository jpaRepository;

    public DoctorRepositoryAdapter(DoctorJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Doctor save(Doctor doctor) {
        return PatientsJpaMapper.toDomain(jpaRepository.save(PatientsJpaMapper.toEntity(doctor)));
    }

    @Override
    public Optional<Doctor> findById(DoctorId id) {
        return jpaRepository.findById(id.value()).map(PatientsJpaMapper::toDomain);
    }

    @Override
    public Optional<Doctor> findByProfileId(UUID profileId) {
        return jpaRepository.findByProfileId(profileId).map(PatientsJpaMapper::toDomain);
    }

    @Override
    public boolean existsByProfileId(UUID profileId) {
        return jpaRepository.existsByProfileId(profileId);
    }

    @Override
    public List<Doctor> findAll() {
        return jpaRepository.findAll().stream().map(PatientsJpaMapper::toDomain).toList();
    }

    @Override
    public boolean existsByDoctorRecordNumber(String doctorRecordNumber) {
        return jpaRepository.existsByDoctorRecordNumber(doctorRecordNumber);
    }
}
