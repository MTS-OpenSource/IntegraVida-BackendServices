package com.integravida.IntegraVidaBackend.medical.application.services;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.DiagnosisRepository;
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

    public DiagnosisCommandService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    public Result<Diagnosis, ApplicationError> create(PatientId patientId,
                                                      DoctorId doctorId,
                                                      String description,
                                                      String recommendation) {
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