package com.integravida.IntegraVidaBackend.monitoring.application.services;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.GlucoseRecordRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRecord;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GlucoseRecordQueryService {
    private final GlucoseRecordRepository repository;

    public GlucoseRecordQueryService(GlucoseRecordRepository repository) {
        this.repository = repository;
    }

    public Result<GlucoseRecord, ApplicationError> getById(UUID recordId) {
        return repository.findById(recordId)
                .map(record -> Result.<GlucoseRecord, ApplicationError>success(record))
                .orElseGet(() -> Result.<GlucoseRecord, ApplicationError>failure(ApplicationError.notFound("glucose-record", recordId.toString())));
    }

    public List<GlucoseRecord> findByPatientId(PatientId patientId) {
        return repository.findByPatientId(patientId);
    }

    public List<GlucoseRecord> findByPatientIdAndMeasuredAtBetween(PatientId patientId, LocalDateTime from, LocalDateTime to) {
        return repository.findByPatientIdAndMeasuredAtBetween(patientId, from, to);
    }
}
