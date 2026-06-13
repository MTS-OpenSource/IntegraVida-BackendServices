package com.integravida.IntegraVidaBackend.monitoring.application.services;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.ClinicalObservationRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.ClinicalObservation;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClinicalObservationQueryService {
    private final ClinicalObservationRepository repository;

    public ClinicalObservationQueryService(ClinicalObservationRepository repository) {
        this.repository = repository;
    }

    public Result<ClinicalObservation, ApplicationError> getById(UUID observationId) {
        return repository.findById(observationId)
                .map(observation -> Result.<ClinicalObservation, ApplicationError>success(observation))
                .orElseGet(() -> Result.<ClinicalObservation, ApplicationError>failure(ApplicationError.notFound("clinical-observation", observationId.toString())));
    }

    public List<ClinicalObservation> findByPatientId(PatientId patientId) {
        return repository.findByPatientId(patientId);
    }
}
