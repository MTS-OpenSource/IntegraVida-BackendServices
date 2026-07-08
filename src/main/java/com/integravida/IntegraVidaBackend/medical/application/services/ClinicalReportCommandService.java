package com.integravida.IntegraVidaBackend.medical.application.services;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.ClinicalReportRepository;
import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.ExternalMonitoringService;
import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.ExternalPatientService;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.ClinicalReport;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientDoctorRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class ClinicalReportCommandService {
    private final ClinicalReportRepository clinicalReportRepository;
    private final ExternalPatientService externalPatientService;
    private final ExternalMonitoringService externalMonitoringService;
    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final PatientDoctorRepository patientDoctorRepository;

    public ClinicalReportCommandService(ClinicalReportRepository clinicalReportRepository,
                                        ExternalPatientService externalPatientService,
                                        ExternalMonitoringService externalMonitoringService,
                                        JwtClaimsExtractor jwtClaimsExtractor,
                                        PatientDoctorRepository patientDoctorRepository) {
        this.clinicalReportRepository = clinicalReportRepository;
        this.externalPatientService = externalPatientService;
        this.externalMonitoringService = externalMonitoringService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
        this.patientDoctorRepository = patientDoctorRepository;
    }

    public Result<ClinicalReport, ApplicationError> create(String title,
                                                           String summary,
                                                           String recommendations) {
        var doctorIdResult = resolveDoctorIdFromJwt();
        if (doctorIdResult instanceof Result.Failure<DoctorId, ApplicationError> failure) {
            return Result.failure(failure.error());
        }

        var doctorId = ((Result.Success<DoctorId, ApplicationError>) doctorIdResult).value();

        var patientIdResult = resolvePatientIdForDoctor(doctorId);
        if (patientIdResult instanceof Result.Failure<PatientId, ApplicationError> failure) {
            return Result.failure(failure.error());
        }

        var patientId = ((Result.Success<PatientId, ApplicationError>) patientIdResult).value();

        if (!externalPatientService.existsById(patientId)) {
            return Result.<ClinicalReport, ApplicationError>failure(
                    ApplicationError.notFound("patient", patientId.value().toString()));
        }

        if (!externalMonitoringService.hasGlucoseRecordsByPatientId(patientId)) {
            return Result.<ClinicalReport, ApplicationError>failure(
                    ApplicationError.notFound("glucose records for patient", patientId.value().toString()));
        }

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

    private Result<DoctorId, ApplicationError> resolveDoctorIdFromJwt() {
        var doctorId = jwtClaimsExtractor.extractDoctorId();

        if (doctorId == null || doctorId.isBlank()) {
            return Result.failure(
                    ApplicationError.validationError("doctorId", "Doctor id claim is required"));
        }

        try {
            return Result.success(DoctorId.fromString(doctorId));
        } catch (IllegalArgumentException exception) {
            return Result.failure(
                    ApplicationError.validationError("doctorId", "Doctor id claim must be a valid UUID"));
        }
    }

    private Result<PatientId, ApplicationError> resolvePatientIdForDoctor(DoctorId doctorId) {
        var patientIdFromToken = jwtClaimsExtractor.extractPatientId();

        if (patientIdFromToken != null && !patientIdFromToken.isBlank()) {
            try {
                return Result.success(PatientId.fromString(patientIdFromToken));
            } catch (IllegalArgumentException exception) {
                return Result.failure(
                        ApplicationError.validationError("patientId", "Patient id claim must be a valid UUID"));
            }
        }

        return patientDoctorRepository.findByDoctorId(doctorId.value())
                .stream()
                .findFirst()
                .map(assignment -> Result.<PatientId, ApplicationError>success(
                        PatientId.of(assignment.getPatientId())))
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("patient assignment", doctorId.value().toString())));
    }
}