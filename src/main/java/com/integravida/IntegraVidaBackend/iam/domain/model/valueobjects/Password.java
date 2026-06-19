package com.integravida.IntegraVidaBackend.iam.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Password(
        @Column(name = "password", nullable = false)
        String password
) {
    public Password {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Error: Password required");
        }
    }
}
