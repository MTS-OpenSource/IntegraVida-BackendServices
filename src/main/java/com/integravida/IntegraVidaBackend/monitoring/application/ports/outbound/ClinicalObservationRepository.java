package com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.ClinicalObservation;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClinicalObservationRepository {
    ClinicalObservation save(ClinicalObservation clinicalObservation);

    Optional<ClinicalObservation> findById(UUID id);

    List<ClinicalObservation> findByPatientId(PatientId patientId);

    void deleteById(UUID id);
}
