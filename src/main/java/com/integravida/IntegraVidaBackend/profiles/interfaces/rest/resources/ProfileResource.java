package com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProfileResource(
        UUID id,
        String firstName,
        String lastName,
        String fullName,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProfileResource fromDomain(Profile p) {
        return new ProfileResource(
                p.getId(),
                p.getName().firstName(),
                p.getName().lastName(),
                p.getName().fullName(),
                p.getEmail().value(),
                p.getPhoneNumber().value(),
                p.getDateOfBirth().value(),
                p.getCreatedAt(),
                p.getUpdatedAt());
    }
}
