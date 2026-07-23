package com.integravida.IntegraVidaBackend.medical.application.services;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.AppointmentRepository;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Appointment;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AppointmentCommandService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentCommandService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Result<Appointment, ApplicationError> create(UUID patientId,
                                                        UUID doctorId,
                                                        LocalDateTime scheduledAt,
                                                        String reason) {
        if (patientId == null) {
            return Result.failure(
                    ApplicationError.validationError("patientId", "patientId is required"));
        }

        if (doctorId == null) {
            return Result.failure(
                    ApplicationError.validationError("doctorId", "doctorId is required"));
        }

        var now = LocalDateTime.now();

        var appointment = Appointment.create(
                UUID.randomUUID(),
                PatientId.of(patientId),
                DoctorId.of(doctorId),
                scheduledAt,
                reason,
                now);

        return Result.<Appointment, ApplicationError>success(appointmentRepository.save(appointment));
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