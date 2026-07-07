package com.integravida.IntegraVidaBackend.patients.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class PatientDoctor extends AbstractDomainAggregateRoot<PatientDoctor> {
    private final UUID id;
    private final UUID patientId;
    private final UUID doctorId;
    private final LocalDateTime assignedAt;

    public PatientDoctor(UUID id, UUID patientId, UUID doctorId, LocalDateTime assignedAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.doctorId = Objects.requireNonNull(doctorId, "doctorId is required");
        this.assignedAt = Objects.requireNonNull(assignedAt, "assignedAt is required");
    }

    public static PatientDoctor assign(UUID patientId, UUID doctorId) {
        return new PatientDoctor(UUID.randomUUID(), patientId, doctorId, LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
}
