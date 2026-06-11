package com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources;

import java.time.LocalDate;

public record ProfileResource(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth
) {}
