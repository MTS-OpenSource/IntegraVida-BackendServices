package com.integravida.IntegraVidaBackend.medical.application.ports.outbound;

import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.ClinicalReport;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClinicalReportRepository {
    ClinicalReport save(ClinicalReport clinicalReport);

    Optional<ClinicalReport> findById(UUID id);

    List<ClinicalReport> findAll();

    List<ClinicalReport> findByPatientId(PatientId patientId);
}