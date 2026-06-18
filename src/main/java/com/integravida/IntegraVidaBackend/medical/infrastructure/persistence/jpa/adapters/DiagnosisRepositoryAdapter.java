package com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.DiagnosisRepository;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Diagnosis;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.mappers.MedicalJpaMapper;
import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.repositories.DiagnosisJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DiagnosisRepositoryAdapter implements DiagnosisRepository {
    private final DiagnosisJpaRepository diagnosisJpaRepository;

    public DiagnosisRepositoryAdapter(DiagnosisJpaRepository diagnosisJpaRepository) {
        this.diagnosisJpaRepository = diagnosisJpaRepository;
    }

    @Override
    public Diagnosis save(Diagnosis diagnosis) {
        var diagnosisEntity = MedicalJpaMapper.toEntity(diagnosis);
        var savedDiagnosisEntity = diagnosisJpaRepository.save(diagnosisEntity);
        return MedicalJpaMapper.toDomain(savedDiagnosisEntity);
    }

    @Override
    public Optional<Diagnosis> findById(UUID id) {
        return diagnosisJpaRepository.findById(id)
                .map(MedicalJpaMapper::toDomain);
    }

    @Override
    public List<Diagnosis> findAll() {
        return diagnosisJpaRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(MedicalJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Diagnosis> findByPatientId(PatientId patientId) {
        return diagnosisJpaRepository.findAllByPatientIdOrderByCreatedAtDesc(patientId.value())
                .stream()
                .map(MedicalJpaMapper::toDomain)
                .toList();
    }
}