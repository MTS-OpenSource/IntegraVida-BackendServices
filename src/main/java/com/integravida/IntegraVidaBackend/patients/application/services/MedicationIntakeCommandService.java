package com.integravida.IntegraVidaBackend.patients.application.services;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.MedicationIntakeRepository;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.MedicationRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.MedicationIntake;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class MedicationIntakeCommandService {
    private final MedicationIntakeRepository medicationIntakeRepository;
    private final MedicationRepository medicationRepository;

    public MedicationIntakeCommandService(MedicationIntakeRepository medicationIntakeRepository,
                                          MedicationRepository medicationRepository) {
        this.medicationIntakeRepository = medicationIntakeRepository;
        this.medicationRepository = medicationRepository;
    }

    public Result<MedicationIntake, ApplicationError> register(UUID medicationId,
                                                                UUID patientId,
                                                                LocalDateTime takenAt,
                                                                String notes) {
        return medicationRepository.findById(medicationId)
                .map(medication -> {
                    if (!medication.getPatientId().equals(PatientId.of(patientId))) {
                        return Result.<MedicationIntake, ApplicationError>failure(ApplicationError.conflict("medication-intake", "medication does not belong to patient"));
                    }
                    var intake = MedicationIntake.create(
                            UUID.randomUUID(),
                            medicationId,
                            PatientId.of(patientId),
                            takenAt,
                            notes,
                            LocalDateTime.now());
                    return Result.<MedicationIntake, ApplicationError>success(medicationIntakeRepository.save(intake));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("medication", medicationId.toString())));
    }
}
