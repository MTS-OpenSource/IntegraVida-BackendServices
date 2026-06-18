package com.integravida.IntegraVidaBackend.patients.application.services;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientRepository;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.TreatmentRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Treatment;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.TreatmentStatus;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class TreatmentCommandService {
    private final TreatmentRepository treatmentRepository;
    private final PatientRepository patientRepository;

    public TreatmentCommandService(TreatmentRepository treatmentRepository,
                                   PatientRepository patientRepository) {
        this.treatmentRepository = treatmentRepository;
        this.patientRepository = patientRepository;
    }

    public Result<Treatment, ApplicationError> create(UUID patientId,
                                                      String name,
                                                      String description,
                                                      LocalDate startDate,
                                                      LocalDate endDate) {
        var patientKey = PatientId.of(patientId);
        if (patientRepository.findById(patientKey).isEmpty()) {
            return Result.failure(ApplicationError.notFound("patient", patientId.toString()));
        }
        if (treatmentRepository.findActiveByPatientId(patientKey).isPresent()) {
            return Result.failure(ApplicationError.conflict("treatment", "patient already has an active treatment: " + patientId));
        }

        var treatment = Treatment.create(
                UUID.randomUUID(),
                patientKey,
                name,
                description,
                startDate,
                endDate,
                LocalDateTime.now());
        treatment.activate(LocalDateTime.now());
        return Result.success(treatmentRepository.save(treatment));
    }

    public Result<Treatment, ApplicationError> update(UUID id,
                                                      String name,
                                                      String description,
                                                      LocalDate startDate,
                                                      LocalDate endDate,
                                                      String status) {
        return treatmentRepository.findById(id)
                .map(treatment -> {
                    treatment.update(name, description, startDate, endDate, LocalDateTime.now());
                    if (status != null && !status.isBlank()) {
                        applyStatusChange(treatment, TreatmentStatus.valueOf(status.toUpperCase()));
                    }
                    return Result.<Treatment, ApplicationError>success(treatmentRepository.save(treatment));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("treatment", id.toString())));
    }

    private void applyStatusChange(Treatment treatment, TreatmentStatus status) {
        switch (status) {
            case ACTIVE -> treatment.activate(LocalDateTime.now());
            case PAUSED -> treatment.pause(LocalDateTime.now());
            case COMPLETED -> treatment.complete(LocalDateTime.now());
            case CANCELLED -> treatment.cancel(LocalDateTime.now());
            case PLANNED -> {
            }
        }
    }
}
