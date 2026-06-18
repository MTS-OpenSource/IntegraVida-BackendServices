package com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record PatientId(UUID value) {
    public PatientId {
        Objects.requireNonNull(value, "value is required");
    }

    public static PatientId of(UUID value) {
        return new PatientId(value);
    }
}
