package com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class PersonName {

    private String firstName;
    private String lastName;

    public PersonName(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name must not be blank");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name must not be blank");
        this.firstName = firstName.trim();
        this.lastName  = lastName.trim();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
