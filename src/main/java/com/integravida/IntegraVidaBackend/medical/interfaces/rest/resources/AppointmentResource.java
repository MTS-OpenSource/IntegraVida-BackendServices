package com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.medical.domain.model.aggregates.Appointment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "Appointment", description = "Medical appointment resource")
public record AppointmentResource(
        @Schema(example = "9f2f46d2-5c9b-4f8d-a937-df2f3a2b1b77") UUID id,
        @Schema(example = "1de8f2c5-7c4c-49d4-8fd8-97f2f2f2b101") UUID patientId,
        @Schema(example = "2ee7a314-9b2f-4f8e-8c31-73b3cdd0b221") UUID doctorId,
        @Schema(example = "2026-06-20T10:30:00") LocalDateTime scheduledAt,
        @Schema(example = "PENDING") String status,
        @Schema(example = "Revisión de resultados de laboratorio") String reason,
        @Schema(example = "2026-06-17T09:20:00") LocalDateTime createdAt,
        @Schema(example = "2026-06-17T09:20:00") LocalDateTime updatedAt,
        @Schema(example = "2026-06-18T12:00:00") LocalDateTime cancelledAt) {

    public static AppointmentResource fromDomain(Appointment appointment) {
        return new AppointmentResource(
                appointment.getId(),
                appointment.getPatientId().value(),
                appointment.getDoctorId().value(),
                appointment.getScheduledAt(),
                appointment.getStatus().name(),
                appointment.getReason(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt(),
                appointment.getCancelledAt().orElse(null));
    }
}