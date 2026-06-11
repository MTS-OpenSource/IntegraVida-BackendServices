package com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class EmailAddress {

    private String email;

    public EmailAddress(String email) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email address: " + email);
        this.email = email.toLowerCase().trim();
    }

    @Override
    public String toString() { return email; }
}
