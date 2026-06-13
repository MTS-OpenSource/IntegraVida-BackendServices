package com.integravida.IntegraVidaBackend.monitoring.application.services;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.GlucoseRangeRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRange;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GlucoseRangeQueryService {
    private final GlucoseRangeRepository repository;

    public GlucoseRangeQueryService(GlucoseRangeRepository repository) {
        this.repository = repository;
    }

    public Result<GlucoseRange, ApplicationError> getById(UUID rangeId) {
        return repository.findById(rangeId)
                .map(range -> Result.<GlucoseRange, ApplicationError>success(range))
                .orElseGet(() -> Result.<GlucoseRange, ApplicationError>failure(ApplicationError.notFound("glucose-range", rangeId.toString())));
    }

    public Result<GlucoseRange, ApplicationError> getActiveByPatientId(PatientId patientId) {
        return repository.findActiveByPatientId(patientId)
                .map(range -> Result.<GlucoseRange, ApplicationError>success(range))
                .orElseGet(() -> Result.<GlucoseRange, ApplicationError>failure(ApplicationError.notFound("glucose-range", patientId.value().toString())));
    }

    public List<GlucoseRange> findByPatientId(PatientId patientId) {
        return repository.findByPatientId(patientId);
    }
}
