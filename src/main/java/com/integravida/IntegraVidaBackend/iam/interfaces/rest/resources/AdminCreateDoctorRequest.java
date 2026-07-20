package com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(name = "AdminCreateDoctorRequest", description = "Payload for admin to create a doctor user with profile")
public record AdminCreateDoctorRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank @Email String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phoneNumber,
        @NotNull LocalDate dateOfBirth,
        @NotBlank String doctorRecordNumber,
        @NotBlank String doctorNotes) {
}
