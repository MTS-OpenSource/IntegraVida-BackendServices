package com.integravida.IntegraVidaBackend.medical.application.services;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.ClinicalReportRepository;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.ClinicalReport;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClinicalReportQueryService {
    private final ClinicalReportRepository clinicalReportRepository;

    public ClinicalReportQueryService(ClinicalReportRepository clinicalReportRepository) {
        this.clinicalReportRepository = clinicalReportRepository;
    }

    public Result<ClinicalReport, ApplicationError> getById(UUID clinicalReportId) {
        return clinicalReportRepository.findById(clinicalReportId)
                .map(clinicalReport -> Result.<ClinicalReport, ApplicationError>success(clinicalReport))
                .orElseGet(() -> Result.<ClinicalReport, ApplicationError>failure(
                        ApplicationError.notFound("clinical report", clinicalReportId.toString())));
    }

    public List<ClinicalReport> findAll() {
        return clinicalReportRepository.findAll();
    }

    public List<ClinicalReport> findByPatientId(PatientId patientId) {
        return clinicalReportRepository.findByPatientId(patientId);
    }
}