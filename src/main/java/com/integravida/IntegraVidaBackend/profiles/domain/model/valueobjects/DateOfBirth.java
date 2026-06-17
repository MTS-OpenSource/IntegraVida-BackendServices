package com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects;

import java.time.LocalDate;
import java.util.Objects;

public record DateOfBirth(LocalDate value) {
    public DateOfBirth {
        Objects.requireNonNull(value, "date of birth is required");
        if (value.isAfter(LocalDate.now())) throw new IllegalArgumentException("date of birth cannot be in the future");
    }

    public static DateOfBirth of(LocalDate value) { return new DateOfBirth(value); }
}
