package com.integravida.IntegraVidaBackend.monitoring.application.services;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.ExternalPatientService;
import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.GlucoseRangeRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRange;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class GlucoseRangeCommandService {
    private final GlucoseRangeRepository repository;
    private final ExternalPatientService externalPatientService;

    public GlucoseRangeCommandService(GlucoseRangeRepository repository, ExternalPatientService externalPatientService) {
        this.repository = repository;
        this.externalPatientService = externalPatientService;
    }

    public Result<GlucoseRange, ApplicationError> upsert(PatientId patientId, GlucoseValue minimumValue, GlucoseValue maximumValue) {
        if (!externalPatientService.existsById(patientId)) {
            return Result.<GlucoseRange, ApplicationError>failure(ApplicationError.notFound("patient", patientId.value().toString()));
        }
        var now = LocalDateTime.now();
        var current = repository.findActiveByPatientId(patientId)
                .map(range -> {
                    range.updateRange(minimumValue, maximumValue, now);
                    return range;
                })
                .orElseGet(() -> GlucoseRange.create(UUID.randomUUID(), patientId, minimumValue, maximumValue, now));
        return Result.<GlucoseRange, ApplicationError>success(repository.save(current));
    }

    public Result<GlucoseRange, ApplicationError> deactivate(UUID rangeId) {
        return repository.findById(rangeId)
                .map(range -> {
                    range.deactivate(LocalDateTime.now());
                    return Result.<GlucoseRange, ApplicationError>success(repository.save(range));
                })
                .orElseGet(() -> Result.<GlucoseRange, ApplicationError>failure(ApplicationError.notFound("glucose-range", rangeId.toString())));
    }
}
