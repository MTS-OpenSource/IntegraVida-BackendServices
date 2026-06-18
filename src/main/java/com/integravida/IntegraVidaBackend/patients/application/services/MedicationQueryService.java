package com.integravida.IntegraVidaBackend.patients.application.services;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.MedicationRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Medication;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MedicationQueryService {
    private final MedicationRepository medicationRepository;

    public MedicationQueryService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public Result<Medication, ApplicationError> getById(UUID id) {
        return medicationRepository.findById(id)
                .map(Result::<Medication, ApplicationError>success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("medication", id.toString())));
    }

    public List<Medication> getAll() {
        return medicationRepository.findAll();
    }

    public List<Medication> getByPatientId(UUID patientId) {
        return medicationRepository.findByPatientId(PatientId.of(patientId));
    }

    public List<Medication> getByTreatmentId(UUID treatmentId) {
        return medicationRepository.findByTreatmentId(treatmentId);
    }
}
