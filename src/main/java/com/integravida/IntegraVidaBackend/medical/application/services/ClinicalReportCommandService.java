package com.integravida.IntegraVidaBackend.medical.application.services;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.ClinicalReportRepository;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.ClinicalReport;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class ClinicalReportCommandService {
    private final ClinicalReportRepository clinicalReportRepository;

    public ClinicalReportCommandService(ClinicalReportRepository clinicalReportRepository) {
        this.clinicalReportRepository = clinicalReportRepository;
    }

    public Result<ClinicalReport, ApplicationError> create(PatientId patientId,
                                                           DoctorId doctorId,
                                                           String title,
                                                           String summary,
                                                           String recommendations) {
        var now = LocalDateTime.now();

        var clinicalReport = ClinicalReport.create(
                UUID.randomUUID(),
                patientId,
                doctorId,
                title,
                summary,
                recommendations,
                now);

        return Result.<ClinicalReport, ApplicationError>success(
                clinicalReportRepository.save(clinicalReport));
    }
}