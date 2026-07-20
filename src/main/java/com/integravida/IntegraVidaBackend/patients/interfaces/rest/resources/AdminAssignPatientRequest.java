package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(name = "AdminAssignPatientRequest", description = "Payload for admin to assign a patient to any doctor")
public record AdminAssignPatientRequest(
        @NotNull @Schema(example = "22222222-2222-2222-2222-222222222222") UUID patientId,
        @NotNull @Schema(example = "66666666-6666-6666-6666-666666666666") UUID doctorId) {
}
