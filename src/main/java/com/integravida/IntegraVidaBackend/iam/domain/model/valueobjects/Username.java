package com.integravida.IntegraVidaBackend.iam.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Username(
        @Column(name = "username", nullable = false, unique = true)
        String username
) {
    public Username {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
    }
}
