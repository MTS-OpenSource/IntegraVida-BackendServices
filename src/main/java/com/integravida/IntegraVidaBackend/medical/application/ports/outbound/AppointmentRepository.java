package com.integravida.IntegraVidaBackend.medical.application.ports.outbound;

import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Appointment;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);

    Optional<Appointment> findById(UUID id);

    List<Appointment> findByPatientId(PatientId patientId);

    List<Appointment> findByDoctorId(DoctorId doctorId);

    List<Appointment> findAll();

    void deleteById(UUID id);
}