package com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class PhoneNumber {

    private String number;

    public PhoneNumber(String number) {
        if (number == null || number.isBlank())
            throw new IllegalArgumentException("Phone number must not be blank");
        this.number = number.trim();
    }

    @Override
    public String toString() { return number; }
}
