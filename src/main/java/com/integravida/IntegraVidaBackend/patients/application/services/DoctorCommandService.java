package com.integravida.IntegraVidaBackend.patients.application.services;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.DoctorRepository;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.ExternalProfileService;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientDoctorRepository;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Doctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.PatientDoctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class DoctorCommandService {
    private final DoctorRepository doctorRepository;
    private final PatientDoctorRepository patientDoctorRepository;
    private final PatientRepository patientRepository;
    private final ExternalProfileService externalProfileService;

    public DoctorCommandService(DoctorRepository doctorRepository,
                                PatientDoctorRepository patientDoctorRepository,
                                PatientRepository patientRepository,
                                ExternalProfileService externalProfileService) {
        this.doctorRepository = doctorRepository;
        this.patientDoctorRepository = patientDoctorRepository;
        this.patientRepository = patientRepository;
        this.externalProfileService = externalProfileService;
    }

    public Result<Doctor, ApplicationError> create(UUID profileId,
                                                    String doctorRecordNumber,
                                                    String notes) {
        if (!externalProfileService.existsById(profileId)) {
            return Result.failure(ApplicationError.notFound("profile", profileId.toString()));
        }
        if (doctorRepository.existsByProfileId(profileId)) {
            return Result.failure(ApplicationError.conflict("doctor", "profile already linked: " + profileId));
        }
        if (doctorRepository.existsByDoctorRecordNumber(doctorRecordNumber)) {
            return Result.failure(ApplicationError.conflict("doctor", "doctor record already exists: " + doctorRecordNumber));
        }

        var doctor = Doctor.create(
                DoctorId.of(UUID.randomUUID()),
                profileId,
                doctorRecordNumber,
                notes,
                LocalDateTime.now());
        return Result.success(doctorRepository.save(doctor));
    }

    public Result<PatientDoctor, ApplicationError> assignPatient(UUID patientId, UUID doctorId) {
        if (patientRepository.findById(com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId.of(patientId)).isEmpty()) {
            return Result.failure(ApplicationError.notFound("patient", patientId.toString()));
        }
        if (doctorRepository.findById(DoctorId.of(doctorId)).isEmpty()) {
            return Result.failure(ApplicationError.notFound("doctor", doctorId.toString()));
        }
        if (patientDoctorRepository.existsByPatientIdAndDoctorId(patientId, doctorId)) {
            return Result.failure(ApplicationError.conflict("assignment", "patient already assigned to this doctor"));
        }

        var assignment = PatientDoctor.assign(patientId, doctorId);
        return Result.success(patientDoctorRepository.save(assignment));
    }
}
