package com.integravida.IntegraVidaBackend.medical.application.services;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.AppointmentRepository;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Appointment;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppointmentQueryService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentQueryService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Result<Appointment, ApplicationError> getById(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> Result.<Appointment, ApplicationError>success(appointment))
                .orElseGet(() -> Result.<Appointment, ApplicationError>failure(
                        ApplicationError.notFound("appointment", appointmentId.toString())));
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> findByPatientId(PatientId patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
}