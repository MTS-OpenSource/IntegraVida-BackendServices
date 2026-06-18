package com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record UpdatePatientRequest(@NotBlank String notes) {
}
