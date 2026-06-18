package com.integravida.IntegraVidaBackend.patients.application.ports.outbound;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Treatment;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreatmentRepository {
    Treatment save(Treatment treatment);

    Optional<Treatment> findById(UUID id);

    List<Treatment> findAll();

    List<Treatment> findByPatientId(PatientId patientId);

    Optional<Treatment> findActiveByPatientId(PatientId patientId);
}
