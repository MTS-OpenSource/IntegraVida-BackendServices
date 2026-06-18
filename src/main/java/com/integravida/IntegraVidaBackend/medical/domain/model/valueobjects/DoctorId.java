package com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record DoctorId(UUID value) {

    public DoctorId {
        Objects.requireNonNull(value, "doctor id is required");
    }

    public static DoctorId of(UUID value) {
        return new DoctorId(value);
    }

    public static DoctorId fromString(String value) {
        return new DoctorId(UUID.fromString(value));
    }
}