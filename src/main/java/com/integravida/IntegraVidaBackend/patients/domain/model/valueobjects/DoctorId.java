package com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record DoctorId(UUID value) {
    public DoctorId {
        Objects.requireNonNull(value, "value is required");
    }

    public static DoctorId of(UUID value) {
        return new DoctorId(value);
    }
}
