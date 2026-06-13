package com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.Alert;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlertRepository {
    Alert save(Alert alert);

    Optional<Alert> findById(UUID id);

    List<Alert> findByPatientId(PatientId patientId);

    List<Alert> findUnreadByPatientId(PatientId patientId);
}
