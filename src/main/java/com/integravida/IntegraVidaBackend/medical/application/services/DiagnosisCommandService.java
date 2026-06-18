package com.integravida.IntegraVidaBackend.medical.application.services;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.DiagnosisRepository;
import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.ExternalMonitoringService;
import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.ExternalPatientService;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Diagnosis;
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
public class DiagnosisCommandService {
    private final DiagnosisRepository diagnosisRepository;
    private final ExternalPatientService externalPatientService;
    private final ExternalMonitoringService externalMonitoringService;

    public DiagnosisCommandService(DiagnosisRepository diagnosisRepository,
                                   ExternalPatientService externalPatientService,
                                   ExternalMonitoringService externalMonitoringService) {
        this.diagnosisRepository = diagnosisRepository;
        this.externalPatientService = externalPatientService;
        this.externalMonitoringService = externalMonitoringService;
    }

    public Result<Diagnosis, ApplicationError> create(PatientId patientId,
                                                      DoctorId doctorId,
                                                      String description,
                                                      String recommendation) {
        if (!externalPatientService.existsById(patientId)) {
            return Result.<Diagnosis, ApplicationError>failure(
                    ApplicationError.notFound("patient", patientId.value().toString()));
        }

        if (!externalMonitoringService.hasGlucoseRecordsByPatientId(patientId)) {
            return Result.<Diagnosis, ApplicationError>failure(
                    ApplicationError.notFound("glucose records for patient", patientId.value().toString()));
        }

        var now = LocalDateTime.now();

        var diagnosis = Diagnosis.create(
                UUID.randomUUID(),
                patientId,
                doctorId,
                description,
                recommendation,
                now);

        return Result.<Diagnosis, ApplicationError>success(diagnosisRepository.save(diagnosis));
    }
}