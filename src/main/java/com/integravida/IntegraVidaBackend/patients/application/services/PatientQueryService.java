package com.integravida.IntegraVidaBackend.patients.application.services;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PatientQueryService {
    private final PatientRepository patientRepository;

    public PatientQueryService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Result<Patient, ApplicationError> getById(UUID id) {
        return patientRepository.findById(PatientId.of(id))
                .map(Result::<Patient, ApplicationError>success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("patient", id.toString())));
    }

    public Result<Patient, ApplicationError> getByProfileId(UUID profileId) {
        return patientRepository.findByProfileId(profileId)
                .map(Result::<Patient, ApplicationError>success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("patient", profileId.toString())));
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }
}
