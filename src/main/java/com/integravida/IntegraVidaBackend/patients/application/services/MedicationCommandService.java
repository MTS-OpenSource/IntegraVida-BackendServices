package com.integravida.IntegraVidaBackend.patients.application.services;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.MedicationRepository;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientRepository;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.TreatmentRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Medication;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.MedicationSchedule;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class MedicationCommandService {
    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;
    private final TreatmentRepository treatmentRepository;

    public MedicationCommandService(MedicationRepository medicationRepository,
                                    PatientRepository patientRepository,
                                    TreatmentRepository treatmentRepository) {
        this.medicationRepository = medicationRepository;
        this.patientRepository = patientRepository;
        this.treatmentRepository = treatmentRepository;
    }

    public Result<Medication, ApplicationError> create(UUID patientId,
                                                      UUID treatmentId,
                                                      String name,
                                                      String dosage,
                                                      MedicationSchedule schedule) {
        var patientKey = PatientId.of(patientId);
        if (patientRepository.findById(patientKey).isEmpty()) {
            return Result.failure(ApplicationError.notFound("patient", patientId.toString()));
        }
        return treatmentRepository.findById(treatmentId)
                .map(treatment -> {
                    if (!treatment.getPatientId().equals(patientKey)) {
                        return Result.<Medication, ApplicationError>failure(ApplicationError.conflict("medication", "treatment does not belong to patient"));
                    }
                    var medication = Medication.create(
                            UUID.randomUUID(),
                            patientKey,
                            treatmentId,
                            name,
                            dosage,
                            schedule,
                            LocalDateTime.now());
                    return Result.<Medication, ApplicationError>success(medicationRepository.save(medication));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("treatment", treatmentId.toString())));
    }
}
