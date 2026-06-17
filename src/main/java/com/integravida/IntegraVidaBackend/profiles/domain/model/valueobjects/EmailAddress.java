package com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects;

import java.util.Objects;

public record EmailAddress(String value) {
    public EmailAddress {
        Objects.requireNonNull(value, "email is required");
        if (!value.contains("@")) throw new IllegalArgumentException("Invalid email address: " + value);
    }

    public static EmailAddress of(String value) {
        return new EmailAddress(value.toLowerCase().trim());
    }
}
