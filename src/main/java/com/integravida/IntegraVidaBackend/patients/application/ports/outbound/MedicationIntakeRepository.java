package com.integravida.IntegraVidaBackend.patients.application.ports.outbound;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.MedicationIntake;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicationIntakeRepository {
    MedicationIntake save(MedicationIntake medicationIntake);

    Optional<MedicationIntake> findById(UUID id);

    List<MedicationIntake> findByPatientId(PatientId patientId);

    List<MedicationIntake> findByMedicationId(UUID medicationId);
}
