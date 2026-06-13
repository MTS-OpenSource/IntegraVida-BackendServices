package com.integravida.IntegraVidaBackend.monitoring.application.services;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.ClinicalObservationRepository;
import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.ExternalPatientService;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.ClinicalObservation;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class ClinicalObservationCommandService {
    private final ClinicalObservationRepository repository;
    private final ExternalPatientService externalPatientService;

    public ClinicalObservationCommandService(ClinicalObservationRepository repository, ExternalPatientService externalPatientService) {
        this.repository = repository;
        this.externalPatientService = externalPatientService;
    }

    public Result<ClinicalObservation, ApplicationError> create(PatientId patientId,
                                                                String category,
                                                                String title,
                                                                String content,
                                                                LocalDateTime observedAt) {
        if (!externalPatientService.existsById(patientId)) {
            return Result.<ClinicalObservation, ApplicationError>failure(ApplicationError.notFound("patient", patientId.value().toString()));
        }
        var now = LocalDateTime.now();
        var observation = ClinicalObservation.create(UUID.randomUUID(), patientId, category, title, content, observedAt, now);
        return Result.<ClinicalObservation, ApplicationError>success(repository.save(observation));
    }

    public Result<ClinicalObservation, ApplicationError> update(UUID observationId,
                                                                String category,
                                                                String title,
                                                                String content,
                                                                LocalDateTime observedAt) {
        return repository.findById(observationId)
                .map(observation -> {
                    observation.update(category, title, content, observedAt, LocalDateTime.now());
                    return Result.<ClinicalObservation, ApplicationError>success(repository.save(observation));
                })
                .orElseGet(() -> Result.<ClinicalObservation, ApplicationError>failure(ApplicationError.notFound("clinical-observation", observationId.toString())));
    }

    public Result<UUID, ApplicationError> delete(UUID observationId) {
        return repository.findById(observationId)
                .map(observation -> {
                    repository.deleteById(observationId);
                    return Result.<UUID, ApplicationError>success(observationId);
                })
                .orElseGet(() -> Result.<UUID, ApplicationError>failure(ApplicationError.notFound("clinical-observation", observationId.toString())));
    }
}
