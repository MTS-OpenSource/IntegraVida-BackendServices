package com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.ClinicalReportRepository;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.ClinicalReport;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.mappers.MedicalJpaMapper;
import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.repositories.ClinicalReportJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ClinicalReportRepositoryAdapter implements ClinicalReportRepository {
    private final ClinicalReportJpaRepository clinicalReportJpaRepository;

    public ClinicalReportRepositoryAdapter(ClinicalReportJpaRepository clinicalReportJpaRepository) {
        this.clinicalReportJpaRepository = clinicalReportJpaRepository;
    }

    @Override
    public ClinicalReport save(ClinicalReport clinicalReport) {
        var clinicalReportEntity = MedicalJpaMapper.toEntity(clinicalReport);
        var savedClinicalReportEntity = clinicalReportJpaRepository.save(clinicalReportEntity);
        return MedicalJpaMapper.toDomain(savedClinicalReportEntity);
    }

    @Override
    public Optional<ClinicalReport> findById(UUID id) {
        return clinicalReportJpaRepository.findById(id)
                .map(MedicalJpaMapper::toDomain);
    }

    @Override
    public List<ClinicalReport> findAll() {
        return clinicalReportJpaRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(MedicalJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<ClinicalReport> findByPatientId(PatientId patientId) {
        return clinicalReportJpaRepository.findAllByPatientIdOrderByCreatedAtDesc(patientId.value())
                .stream()
                .map(MedicalJpaMapper::toDomain)
                .toList();
    }
}