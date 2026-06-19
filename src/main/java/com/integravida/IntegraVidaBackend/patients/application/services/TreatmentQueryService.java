package com.integravida.IntegraVidaBackend.patients.application.services;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.TreatmentRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Treatment;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TreatmentQueryService {
    private final TreatmentRepository treatmentRepository;

    public TreatmentQueryService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    public Result<Treatment, ApplicationError> getById(UUID id) {
        return treatmentRepository.findById(id)
                .map(Result::<Treatment, ApplicationError>success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("treatment", id.toString())));
    }

    public List<Treatment> getAll() {
        return treatmentRepository.findAll();
    }

    public List<Treatment> getByPatientId(UUID patientId) {
        return treatmentRepository.findByPatientId(PatientId.of(patientId));
    }

    public Result<Treatment, ApplicationError> getActiveByPatientId(UUID patientId) {
        return treatmentRepository.findActiveByPatientId(PatientId.of(patientId))
                .map(Result::<Treatment, ApplicationError>success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("active treatment", patientId.toString())));
    }
}
