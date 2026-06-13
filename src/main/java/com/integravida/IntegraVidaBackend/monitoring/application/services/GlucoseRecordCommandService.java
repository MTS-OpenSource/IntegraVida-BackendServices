package com.integravida.IntegraVidaBackend.monitoring.application.services;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.AlertRepository;
import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.ExternalPatientService;
import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.GlucoseRangeRepository;
import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.GlucoseRecordRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.Alert;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRange;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRecord;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.events.GlucoseAlertTriggeredEvent;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class GlucoseRecordCommandService {
    private static final GlucoseValue DEFAULT_MINIMUM_RANGE = GlucoseValue.of(new BigDecimal("70"));
    private static final GlucoseValue DEFAULT_MAXIMUM_RANGE = GlucoseValue.of(new BigDecimal("180"));

    private final GlucoseRecordRepository recordRepository;
    private final GlucoseRangeRepository rangeRepository;
    private final AlertRepository alertRepository;
    private final ExternalPatientService externalPatientService;

    public GlucoseRecordCommandService(GlucoseRecordRepository recordRepository,
                                       GlucoseRangeRepository rangeRepository,
                                       AlertRepository alertRepository,
                                       ExternalPatientService externalPatientService) {
        this.recordRepository = recordRepository;
        this.rangeRepository = rangeRepository;
        this.alertRepository = alertRepository;
        this.externalPatientService = externalPatientService;
    }

    public Result<GlucoseRecord, ApplicationError> create(PatientId patientId, GlucoseValue glucoseValue, LocalDateTime measuredAt) {
        if (!externalPatientService.existsById(patientId)) {
            return Result.<GlucoseRecord, ApplicationError>failure(ApplicationError.notFound("patient", patientId.value().toString()));
        }
        var range = resolveActiveRange(patientId);
        var now = LocalDateTime.now();
        var record = GlucoseRecord.create(UUID.randomUUID(), patientId, glucoseValue, range, measuredAt, now);
        var saved = recordRepository.save(record);
        publishAlertIfTriggered(saved);
        saved.clearDomainEvents();
        return Result.<GlucoseRecord, ApplicationError>success(saved);
    }

    public Result<GlucoseRecord, ApplicationError> update(UUID recordId, GlucoseValue glucoseValue, LocalDateTime measuredAt) {
        return recordRepository.findById(recordId)
                .map(record -> {
                    var range = resolveActiveRange(record.getPatientId());
                    record.update(glucoseValue, range, measuredAt, LocalDateTime.now());
                    var saved = recordRepository.save(record);
                    publishAlertIfTriggered(saved);
                    saved.clearDomainEvents();
                    return Result.<GlucoseRecord, ApplicationError>success(saved);
                })
                .orElseGet(() -> Result.<GlucoseRecord, ApplicationError>failure(ApplicationError.notFound("glucose-record", recordId.toString())));
    }

    public Result<UUID, ApplicationError> delete(UUID recordId) {
        return recordRepository.findById(recordId)
                .map(record -> {
                    recordRepository.deleteById(recordId);
                    return Result.<UUID, ApplicationError>success(recordId);
                })
                .orElseGet(() -> Result.<UUID, ApplicationError>failure(ApplicationError.notFound("glucose-record", recordId.toString())));
    }

    private GlucoseRange resolveActiveRange(PatientId patientId) {
        return rangeRepository.findActiveByPatientId(patientId)
                .orElseGet(() -> {
                    var range = GlucoseRange.create(UUID.randomUUID(), patientId, DEFAULT_MINIMUM_RANGE, DEFAULT_MAXIMUM_RANGE, LocalDateTime.now());
                    return rangeRepository.save(range);
                });
    }

    private void publishAlertIfTriggered(GlucoseRecord record) {
        record.getTriggeredSeverity().ifPresent(severity -> record.domainEvents().stream()
                .filter(GlucoseAlertTriggeredEvent.class::isInstance)
                .map(GlucoseAlertTriggeredEvent.class::cast)
                .map(event -> Alert.fromGlucoseAlertTriggeredEvent(UUID.randomUUID(), event))
                .forEach(alertRepository::save));
    }
}
