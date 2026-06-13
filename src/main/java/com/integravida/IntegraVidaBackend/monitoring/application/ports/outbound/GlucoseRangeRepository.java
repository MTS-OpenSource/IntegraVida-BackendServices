package com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRange;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GlucoseRangeRepository {
    GlucoseRange save(GlucoseRange glucoseRange);

    Optional<GlucoseRange> findById(UUID id);

    Optional<GlucoseRange> findActiveByPatientId(PatientId patientId);

    List<GlucoseRange> findByPatientId(PatientId patientId);
}
