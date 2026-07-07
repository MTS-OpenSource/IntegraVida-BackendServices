package com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpResource(
        @NotBlank(message = "El username es obligatorio") String username,
        @NotBlank(message = "La contraseña es obligatoria") String password,
        @NotBlank(message = "El email es obligatorio") @Email(message = "Formato de email inválido") String email,
        @NotNull(message = "El rol es obligatorio") String role
) {}