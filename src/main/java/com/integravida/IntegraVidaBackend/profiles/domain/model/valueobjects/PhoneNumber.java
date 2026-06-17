package com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects;

import java.util.Objects;

public record PhoneNumber(String value) {
    public PhoneNumber {
        Objects.requireNonNull(value, "phone number is required");
        if (value.isBlank()) throw new IllegalArgumentException("phone number must not be blank");
    }

    public static PhoneNumber of(String value) { return new PhoneNumber(value.trim()); }
}
