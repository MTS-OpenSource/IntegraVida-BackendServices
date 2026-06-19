package com.integravida.IntegraVidaBackend.patients.application.ports.outbound;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository {
    Patient save(Patient patient);

    Optional<Patient> findById(PatientId id);

    Optional<Patient> findByProfileId(UUID profileId);

    List<Patient> findAll();

    boolean existsByProfileId(UUID profileId);

    boolean existsByMedicalRecordNumber(String medicalRecordNumber);
}
