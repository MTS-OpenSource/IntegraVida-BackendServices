package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.PatientDoctor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "PatientAssignment", description = "Assignment of a patient to a doctor")
public record PatientAssignmentResource(UUID id,
                                        @Schema(example = "22222222-2222-2222-2222-222222222222")
                                        UUID patientId,
                                        @Schema(example = "66666666-6666-6666-6666-666666666666")
                                        UUID doctorId,
                                        @Schema(example = "2026-06-13T08:30:01")
                                        LocalDateTime assignedAt) {
    public static PatientAssignmentResource fromDomain(PatientDoctor patientDoctor) {
        return new PatientAssignmentResource(
                patientDoctor.getId(),
                patientDoctor.getPatientId(),
                patientDoctor.getDoctorId(),
                patientDoctor.getAssignedAt());
    }
}
