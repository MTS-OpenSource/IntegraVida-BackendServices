package com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRecord;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GlucoseRecordRepository {
    GlucoseRecord save(GlucoseRecord glucoseRecord);

    Optional<GlucoseRecord> findById(UUID id);

    List<GlucoseRecord> findByPatientIdAndMeasuredAtBetween(PatientId patientId, LocalDateTime from, LocalDateTime to);

    List<GlucoseRecord> findByPatientId(PatientId patientId);

    void deleteById(UUID id);
}
