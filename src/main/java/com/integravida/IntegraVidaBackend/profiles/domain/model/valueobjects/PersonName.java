package com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects;

import java.util.Objects;

public record PersonName(String firstName, String lastName) {
    public PersonName {
        Objects.requireNonNull(firstName, "firstName is required");
        Objects.requireNonNull(lastName, "lastName is required");
        if (firstName.isBlank()) throw new IllegalArgumentException("firstName must not be blank");
        if (lastName.isBlank())  throw new IllegalArgumentException("lastName must not be blank");
    }

    public static PersonName of(String firstName, String lastName) {
        return new PersonName(firstName.trim(), lastName.trim());
    }

    public String fullName() { return firstName + " " + lastName; }
}
