package com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources;

import java.time.LocalDate;

public record UpdateProfileResource(
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate dateOfBirth
) {}
