package com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.AppointmentRepository;
import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Appointment;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.mappers.MedicalJpaMapper;
import com.integravida.IntegraVidaBackend.medical.infrastructure.persistence.jpa.repositories.AppointmentJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AppointmentRepositoryAdapter implements AppointmentRepository {
    private final AppointmentJpaRepository appointmentJpaRepository;

    public AppointmentRepositoryAdapter(AppointmentJpaRepository appointmentJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
    }

    @Override
    public Appointment save(Appointment appointment) {
        var appointmentEntity = MedicalJpaMapper.toEntity(appointment);
        var savedAppointmentEntity = appointmentJpaRepository.save(appointmentEntity);
        return MedicalJpaMapper.toDomain(savedAppointmentEntity);
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return appointmentJpaRepository.findById(id)
                .map(MedicalJpaMapper::toDomain);
    }

    @Override
    public List<Appointment> findByPatientId(PatientId patientId) {
        return appointmentJpaRepository.findAllByPatientIdOrderByScheduledAtDesc(patientId.value())
                .stream()
                .map(MedicalJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Appointment> findByDoctorId(DoctorId doctorId) {
        return appointmentJpaRepository.findAllByDoctorIdOrderByScheduledAtDesc(doctorId.value())
                .stream()
                .map(MedicalJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentJpaRepository.findAllByOrderByScheduledAtDesc()
                .stream()
                .map(MedicalJpaMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        appointmentJpaRepository.deleteById(id);
    }
}