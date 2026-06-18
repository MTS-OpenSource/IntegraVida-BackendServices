package com.integravida.IntegraVidaBackend.patients.application.services;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.ExternalProfileService;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PatientCommandService {
    private final PatientRepository patientRepository;
    private final ExternalProfileService externalProfileService;

    public PatientCommandService(PatientRepository patientRepository,
                                 ExternalProfileService externalProfileService) {
        this.patientRepository = patientRepository;
        this.externalProfileService = externalProfileService;
    }

    public Result<Patient, ApplicationError> create(UUID profileId,
                                                    String medicalRecordNumber,
                                                    String notes) {
        if (!externalProfileService.existsById(profileId)) {
            return Result.failure(ApplicationError.notFound("profile", profileId.toString()));
        }
        if (patientRepository.existsByProfileId(profileId)) {
            return Result.failure(ApplicationError.conflict("patient", "profile already linked: " + profileId));
        }
        if (patientRepository.existsByMedicalRecordNumber(medicalRecordNumber)) {
            return Result.failure(ApplicationError.conflict("patient", "medical record already exists: " + medicalRecordNumber));
        }

        var patient = Patient.create(
                PatientId.of(UUID.randomUUID()),
                profileId,
                medicalRecordNumber,
                notes,
                LocalDateTime.now());
        return Result.success(patientRepository.save(patient));
    }

    public Result<Patient, ApplicationError> updateNotes(UUID id, String notes) {
        return patientRepository.findById(PatientId.of(id))
                .map(patient -> {
                    patient.updateNotes(notes, LocalDateTime.now());
                    return Result.<Patient, ApplicationError>success(patientRepository.save(patient));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("patient", id.toString())));
    }

    public Result<Patient, ApplicationError> deactivate(UUID id) {
        return patientRepository.findById(PatientId.of(id))
                .map(patient -> {
                    patient.deactivate(LocalDateTime.now());
                    return Result.<Patient, ApplicationError>success(patientRepository.save(patient));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("patient", id.toString())));
    }

    public Result<Patient, ApplicationError> reactivate(UUID id) {
        return patientRepository.findById(PatientId.of(id))
                .map(patient -> {
                    patient.reactivate(LocalDateTime.now());
                    return Result.<Patient, ApplicationError>success(patientRepository.save(patient));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("patient", id.toString())));
    }
}
