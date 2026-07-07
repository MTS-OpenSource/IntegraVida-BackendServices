package com.integravida.IntegraVidaBackend.patients.application.services;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.MedicationIntakeRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.MedicationIntake;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MedicationIntakeQueryService {
    private final MedicationIntakeRepository medicationIntakeRepository;

    public MedicationIntakeQueryService(MedicationIntakeRepository medicationIntakeRepository) {
        this.medicationIntakeRepository = medicationIntakeRepository;
    }

    public List<MedicationIntake> getByPatientId(UUID patientId) {
        return medicationIntakeRepository.findByPatientId(PatientId.of(patientId));
    }
}
