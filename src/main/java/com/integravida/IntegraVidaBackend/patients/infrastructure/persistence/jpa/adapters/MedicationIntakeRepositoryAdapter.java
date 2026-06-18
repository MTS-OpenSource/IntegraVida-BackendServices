package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.MedicationIntakeRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.MedicationIntake;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.mappers.PatientsJpaMapper;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.repositories.MedicationIntakeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MedicationIntakeRepositoryAdapter implements MedicationIntakeRepository {
    private final MedicationIntakeJpaRepository jpaRepository;

    public MedicationIntakeRepositoryAdapter(MedicationIntakeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MedicationIntake save(MedicationIntake medicationIntake) {
        return PatientsJpaMapper.toDomain(jpaRepository.save(PatientsJpaMapper.toEntity(medicationIntake)));
    }

    @Override
    public Optional<MedicationIntake> findById(UUID id) {
        return jpaRepository.findById(id).map(PatientsJpaMapper::toDomain);
    }

    @Override
    public List<MedicationIntake> findByPatientId(PatientId patientId) {
        return jpaRepository.findAllByPatientIdOrderByCreatedAtDesc(patientId.value()).stream().map(PatientsJpaMapper::toDomain).toList();
    }

    @Override
    public List<MedicationIntake> findByMedicationId(UUID medicationId) {
        return jpaRepository.findAllByMedicationIdOrderByCreatedAtDesc(medicationId).stream().map(PatientsJpaMapper::toDomain).toList();
    }
}
