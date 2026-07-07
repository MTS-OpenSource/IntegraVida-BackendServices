package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(name = "AssignPatientRequest", description = "Payload to assign a patient to a doctor")
public record AssignPatientRequest(
        @NotNull @Schema(example = "22222222-2222-2222-2222-222222222222") UUID patientId) {
}
