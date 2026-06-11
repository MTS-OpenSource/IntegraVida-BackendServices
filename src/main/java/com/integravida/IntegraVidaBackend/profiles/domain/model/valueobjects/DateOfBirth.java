package com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor
public class DateOfBirth {

    private LocalDate date;

    public DateOfBirth(LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("Date of birth must not be null");
        if (date.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        this.date = date;
    }

    @Override
    public String toString() { return date != null ? date.toString() : ""; }
}
