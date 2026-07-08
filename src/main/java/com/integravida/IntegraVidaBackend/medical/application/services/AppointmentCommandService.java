package com.integravida.IntegraVidaBackend.medical.application.services;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.AppointmentRepository;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Appointment;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientDoctorRepository;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AppointmentCommandService {
    private final AppointmentRepository appointmentRepository;
    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final PatientDoctorRepository patientDoctorRepository;

    public AppointmentCommandService(AppointmentRepository appointmentRepository,
                                     JwtClaimsExtractor jwtClaimsExtractor,
                                     PatientDoctorRepository patientDoctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
        this.patientDoctorRepository = patientDoctorRepository;
    }

    public Result<Appointment, ApplicationError> create(LocalDateTime scheduledAt,
                                                        String reason) {
        var patientIdResult = resolvePatientIdFromJwt();
        if (patientIdResult instanceof Result.Failure<PatientId, ApplicationError> failure) {
            return Result.failure(failure.error());
        }

        var patientId = ((Result.Success<PatientId, ApplicationError>) patientIdResult).value();

        var doctorIdResult = resolveDoctorIdForPatient(patientId);
        if (doctorIdResult instanceof Result.Failure<DoctorId, ApplicationError> failure) {
            return Result.failure(failure.error());
        }

        var doctorId = ((Result.Success<DoctorId, ApplicationError>) doctorIdResult).value();

        var now = LocalDateTime.now();

        var appointment = Appointment.create(
                UUID.randomUUID(),
                patientId,
                doctorId,
                scheduledAt,
                reason,
                now);

        return Result.<Appointment, ApplicationError>success(appointmentRepository.save(appointment));
    }

    private Result<PatientId, ApplicationError> resolvePatientIdFromJwt() {
        var patientId = jwtClaimsExtractor.extractPatientId();

        if (patientId == null || patientId.isBlank()) {
            return Result.failure(
                    ApplicationError.validationError("patientId", "Patient id claim is required"));
        }

        try {
            return Result.success(PatientId.fromString(patientId));
        } catch (IllegalArgumentException exception) {
            return Result.failure(
                    ApplicationError.validationError("patientId", "Patient id claim must be a valid UUID"));
        }
    }

    private Result<DoctorId, ApplicationError> resolveDoctorIdForPatient(PatientId patientId) {
        var doctorIdFromToken = jwtClaimsExtractor.extractDoctorId();

        if (doctorIdFromToken != null && !doctorIdFromToken.isBlank()) {
            try {
                return Result.success(DoctorId.fromString(doctorIdFromToken));
            } catch (IllegalArgumentException exception) {
                return Result.failure(
                        ApplicationError.validationError("doctorId", "Doctor id claim must be a valid UUID"));
            }
        }

        return patientDoctorRepository.findByPatientId(patientId.value())
                .map(assignment -> Result.<DoctorId, ApplicationError>success(
                        DoctorId.of(assignment.getDoctorId())))
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("doctor assignment", patientId.value().toString())));
    }

    public Result<Appointment, ApplicationError> update(UUID appointmentId,
                                                        LocalDateTime scheduledAt,
                                                        String reason) {
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> {
                    appointment.reschedule(scheduledAt, LocalDateTime.now());
                    appointment.updateReason(reason, LocalDateTime.now());
                    return Result.<Appointment, ApplicationError>success(appointmentRepository.save(appointment));
                })
                .orElseGet(() -> Result.<Appointment, ApplicationError>failure(
                        ApplicationError.notFound("appointment", appointmentId.toString())));
    }

    public Result<Appointment, ApplicationError> confirm(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> {
                    appointment.confirm(LocalDateTime.now());
                    return Result.<Appointment, ApplicationError>success(appointmentRepository.save(appointment));
                })
                .orElseGet(() -> Result.<Appointment, ApplicationError>failure(
                        ApplicationError.notFound("appointment", appointmentId.toString())));
    }

    public Result<Appointment, ApplicationError> cancel(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> {
                    appointment.cancel(LocalDateTime.now());
                    return Result.<Appointment, ApplicationError>success(appointmentRepository.save(appointment));
                })
                .orElseGet(() -> Result.<Appointment, ApplicationError>failure(
                        ApplicationError.notFound("appointment", appointmentId.toString())));
    }

    public Result<UUID, ApplicationError> delete(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> {
                    appointmentRepository.deleteById(appointmentId);
                    return Result.<UUID, ApplicationError>success(appointmentId);
                })
                .orElseGet(() -> Result.<UUID, ApplicationError>failure(
                        ApplicationError.notFound("appointment", appointmentId.toString())));
    }
}