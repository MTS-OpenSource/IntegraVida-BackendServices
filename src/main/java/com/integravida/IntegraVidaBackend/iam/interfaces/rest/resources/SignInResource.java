package com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record SignInResource(
        @NotBlank(message = "El username es obligatorio") String username,
        @NotBlank(message = "La contraseña es obligatoria") String password
) {}