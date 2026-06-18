package com.integravida.IntegraVidaBackend.medical.application.ports.outbound;

import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Diagnosis;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiagnosisRepository {
    Diagnosis save(Diagnosis diagnosis);

    Optional<Diagnosis> findById(UUID id);

    List<Diagnosis> findAll();

    List<Diagnosis> findByPatientId(PatientId patientId);
}