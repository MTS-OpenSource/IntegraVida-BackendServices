package com.integravida.IntegraVidaBackend.medical.application.services;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.DiagnosisRepository;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Diagnosis;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DiagnosisQueryService {
    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisQueryService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    public Result<Diagnosis, ApplicationError> getById(UUID diagnosisId) {
        return diagnosisRepository.findById(diagnosisId)
                .map(diagnosis -> Result.<Diagnosis, ApplicationError>success(diagnosis))
                .orElseGet(() -> Result.<Diagnosis, ApplicationError>failure(
                        ApplicationError.notFound("diagnosis", diagnosisId.toString())));
    }

    public List<Diagnosis> findAll() {
        return diagnosisRepository.findAll();
    }

    public List<Diagnosis> findByPatientId(PatientId patientId) {
        return diagnosisRepository.findByPatientId(patientId);
    }
}