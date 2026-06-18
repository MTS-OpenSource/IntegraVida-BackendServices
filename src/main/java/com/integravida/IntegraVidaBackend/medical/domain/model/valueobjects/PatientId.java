package com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record PatientId(UUID value) {

    public PatientId {
        Objects.requireNonNull(value, "patient id is required");
    }

    public static PatientId of(UUID value) {
        return new PatientId(value);
    }

    public static PatientId fromString(String value) {
        return new PatientId(UUID.fromString(value));
    }
}