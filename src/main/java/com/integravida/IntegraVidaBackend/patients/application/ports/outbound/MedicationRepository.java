package com.integravida.IntegraVidaBackend.patients.application.ports.outbound;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Medication;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicationRepository {
    Medication save(Medication medication);

    Optional<Medication> findById(UUID id);

    List<Medication> findAll();

    List<Medication> findByPatientId(PatientId patientId);

    List<Medication> findByTreatmentId(UUID treatmentId);
}
