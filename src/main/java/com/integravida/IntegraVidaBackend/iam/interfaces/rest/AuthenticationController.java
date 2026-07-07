package com.integravida.IntegraVidaBackend.iam.interfaces.rest;

import com.integravida.IntegraVidaBackend.iam.application.internal.commandservices.UserCommandServiceImpl;
import com.integravida.IntegraVidaBackend.iam.domain.model.Roles;
import com.integravida.IntegraVidaBackend.iam.domain.model.User;
import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.TokenService;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.SignInResource;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.SignUpResource;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
@Tag(name = "Authentication", description = "Endpoints de autenticación y registro de usuarios")
public class AuthenticationController {

    private final UserCommandServiceImpl userCommandService;
    private final TokenService tokenService;

    public AuthenticationController(UserCommandServiceImpl userCommandService, TokenService tokenService) {
        this.userCommandService = userCommandService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Registrar un nuevo usuario (Sign-Up)", description = "Crea una cuenta para un Patient o Doctor y emite el token extendido.")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpResource resource) {
        Roles requestedRole = Roles.valueOf(resource.role().toUpperCase());
        Result<User, ApplicationError> result = userCommandService.signUp(resource.username(), resource.password(), resource.email(), requestedRole);

        if (result instanceof Result.Failure<User, ApplicationError> failure) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(failure.error().message());
        }

        User newUser = result.toOptional().orElseThrow();

        Long generatedProfileId = 100L + newUser.getId();
        Long generatedPatientId = requestedRole == Roles.PATIENT ? 200L + newUser.getId() : null;
        Long generatedDoctorId = requestedRole == Roles.DOCTOR ? 300L + newUser.getId() : null;

        String token = tokenService.generateToken(
                newUser.getUsername().username(),
                newUser.getId(),
                newUser.getRole().name(),
                generatedProfileId,
                generatedPatientId,
                generatedDoctorId
        );

        return new ResponseEntity<>(new AuthenticatedUserResource(String.valueOf(newUser.getId()), token), HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesión (Sign-In)", description = "Autentica al usuario y devuelve el JWT con claims extendidos.")
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInResource resource) {
        Result<User, ApplicationError> result = userCommandService.verifyCredentials(resource.username(), resource.password());

        if (result.isFailure()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        User user = result.toOptional().orElseThrow();

        Long fetchedProfileId = 100L + user.getId();
        Long fetchedPatientId = user.getRole() == Roles.PATIENT ? 200L + user.getId() : null;
        Long fetchedDoctorId = user.getRole() == Roles.DOCTOR ? 300L + user.getId() : null;

        String token = tokenService.generateToken(
                user.getUsername().username(),
                user.getId(),
                user.getRole().name(),
                fetchedProfileId,
                fetchedPatientId,
                fetchedDoctorId
        );

        return ResponseEntity.ok(new AuthenticatedUserResource(String.valueOf(user.getId()), token));
    }
}