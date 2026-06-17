package com.integravida.IntegraVidaBackend.medical.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.AppointmentStatus;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class Appointment extends AbstractDomainAggregateRoot<Appointment> {
    private final UUID id;
    private final PatientId patientId;
    private final DoctorId doctorId;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;
    private String reason;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime cancelledAt;

    private Appointment(UUID id,
                        PatientId patientId,
                        DoctorId doctorId,
                        LocalDateTime scheduledAt,
                        AppointmentStatus status,
                        String reason,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt,
                        LocalDateTime cancelledAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.patientId = Objects.requireNonNull(patientId, "patientId is required");
        this.doctorId = Objects.requireNonNull(doctorId, "doctorId is required");
        this.scheduledAt = Objects.requireNonNull(scheduledAt, "scheduledAt is required");
        this.status = Objects.requireNonNull(status, "status is required");
        this.reason = Objects.requireNonNull(reason, "reason is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
        this.cancelledAt = cancelledAt;
    }

    public static Appointment create(UUID id,
                                     PatientId patientId,
                                     DoctorId doctorId,
                                     LocalDateTime scheduledAt,
                                     String reason,
                                     LocalDateTime createdAt) {
        return new Appointment(
                id,
                patientId,
                doctorId,
                scheduledAt,
                AppointmentStatus.PENDING,
                reason,
                createdAt,
                createdAt,
                null);
    }

    public static Appointment reconstitute(UUID id,
                                           PatientId patientId,
                                           DoctorId doctorId,
                                           LocalDateTime scheduledAt,
                                           AppointmentStatus status,
                                           String reason,
                                           LocalDateTime createdAt,
                                           LocalDateTime updatedAt,
                                           LocalDateTime cancelledAt) {
        return new Appointment(
                id,
                patientId,
                doctorId,
                scheduledAt,
                status,
                reason,
                createdAt,
                updatedAt,
                cancelledAt);
    }

    public void confirm(LocalDateTime updatedAt) {
        if (this.status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("cancelled appointments cannot be confirmed");
        }

        this.status = AppointmentStatus.CONFIRMED;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void cancel(LocalDateTime cancelledAt) {
        this.status = AppointmentStatus.CANCELLED;
        this.cancelledAt = Objects.requireNonNull(cancelledAt, "cancelledAt is required");
        this.updatedAt = cancelledAt;
    }

    public void reschedule(LocalDateTime scheduledAt, LocalDateTime updatedAt) {
        if (this.status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("cancelled appointments cannot be rescheduled");
        }

        this.scheduledAt = Objects.requireNonNull(scheduledAt, "scheduledAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public void updateReason(String reason, LocalDateTime updatedAt) {
        if (this.status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("cancelled appointments cannot be updated");
        }

        this.reason = Objects.requireNonNull(reason, "reason is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public UUID getId() {
        return id;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public DoctorId getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Optional<LocalDateTime> getCancelledAt() {
        return Optional.ofNullable(cancelledAt);
    }
}