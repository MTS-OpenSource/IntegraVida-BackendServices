package com.integravida.IntegraVidaBackend.iam.interfaces.rest;

import com.integravida.IntegraVidaBackend.iam.application.internal.commandservices.UserCommandServiceImpl;
import com.integravida.IntegraVidaBackend.iam.domain.model.Roles;
import com.integravida.IntegraVidaBackend.iam.domain.model.User;
import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.TokenService;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.SignInResource;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.SignUpResource;
import com.integravida.IntegraVidaBackend.patients.application.services.DoctorCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.DoctorQueryService;
import com.integravida.IntegraVidaBackend.patients.application.services.PatientCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.PatientQueryService;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Doctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient;
import com.integravida.IntegraVidaBackend.profiles.application.services.ProfileCommandService;
import com.integravida.IntegraVidaBackend.profiles.application.services.ProfileQueryService;
import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/authentication")
@Tag(name = "Authentication", description = "Endpoints de autenticación y registro de usuarios")
public class AuthenticationController {

    private final UserCommandServiceImpl userCommandService;
    private final TokenService tokenService;
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;
    private final PatientCommandService patientCommandService;
    private final PatientQueryService patientQueryService;
    private final DoctorCommandService doctorCommandService;
    private final DoctorQueryService doctorQueryService;

    public AuthenticationController(UserCommandServiceImpl userCommandService,
                                    TokenService tokenService,
                                    ProfileCommandService profileCommandService,
                                    ProfileQueryService profileQueryService,
                                    PatientCommandService patientCommandService,
                                    PatientQueryService patientQueryService,
                                    DoctorCommandService doctorCommandService,
                                    DoctorQueryService doctorQueryService) {
        this.userCommandService = userCommandService;
        this.tokenService = tokenService;
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
        this.patientCommandService = patientCommandService;
        this.patientQueryService = patientQueryService;
        this.doctorCommandService = doctorCommandService;
        this.doctorQueryService = doctorQueryService;
    }

    @Operation(summary = "Registrar un nuevo usuario (Sign-Up)", description = "Crea un usuario, su perfil y paciente (si el rol es PATIENT), y emite el token.")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpResource resource) {
        Roles requestedRole;
        try {
            requestedRole = Roles.valueOf(resource.role().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body("Rol inválido. Roles permitidos para registro público: PATIENT");
        }

        if (requestedRole != Roles.PATIENT) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("El registro público solo permite usuarios PATIENT");
        }

        Result<User, ApplicationError> userResult = userCommandService.signUp(resource.username(), resource.password(), resource.email(), requestedRole);

        if (userResult instanceof Result.Failure<User, ApplicationError> failure) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(failure.error().message());
        }

        User newUser = userResult.toOptional().orElseThrow();

        // Crear Profile con los mismos datos del sign-up
        Result<Profile, ApplicationError> profileResult = profileCommandService.create(
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.phoneNumber(),
                resource.dateOfBirth());

        if (profileResult instanceof Result.Failure<Profile, ApplicationError> failure) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(failure.error().message());
        }

        Profile profile = profileResult.toOptional().orElseThrow();
        String profileId = profile.getId().toString();

        String patientId = null;
        if (requestedRole == Roles.PATIENT) {
            String medicalRecordNumber = "MRN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Result<Patient, ApplicationError> patientResult = patientCommandService.create(
                    profile.getId(),
                    medicalRecordNumber,
                    "Patient created during sign-up");

            if (patientResult instanceof Result.Failure<Patient, ApplicationError> failure) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(failure.error().message());
            }

            patientId = patientResult.toOptional().orElseThrow().getId().value().toString();
        }

        String doctorId = null;
        if (requestedRole == Roles.DOCTOR) {
            String doctorRecordNumber = "DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Result<Doctor, ApplicationError> doctorResult = doctorCommandService.create(
                    profile.getId(),
                    doctorRecordNumber,
                    "Doctor created during sign-up");

            if (doctorResult instanceof Result.Failure<Doctor, ApplicationError> failure) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(failure.error().message());
            }

            doctorId = doctorResult.toOptional().orElseThrow().getId().value().toString();
        }

        String token = tokenService.generateToken(
                newUser.getUsername().username(),
                newUser.getId(),
                newUser.getRole().name(),
                profileId,
                patientId,
                doctorId);

        return new ResponseEntity<>(new AuthenticatedUserResource(String.valueOf(newUser.getId()), token), HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesión (Sign-In)", description = "Autentica al usuario y devuelve el JWT con claims extendidos.")
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInResource resource) {
        Result<User, ApplicationError> authResult = userCommandService.verifyCredentials(resource.username(), resource.password());

        if (authResult.isFailure()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        User user = authResult.toOptional().orElseThrow();

        // Buscar Profile por el email del usuario
        Result<Profile, ApplicationError> profileResult = profileQueryService.getByEmail(user.getEmail());

        if (profileResult instanceof Result.Failure<Profile, ApplicationError>) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Perfil no encontrado para el usuario");
        }

        Profile profile = profileResult.toOptional().orElseThrow();
        String profileId = profile.getId().toString();

        String patientId = null;
        if (user.getRole() == Roles.PATIENT) {
            Result<Patient, ApplicationError> patientResult = patientQueryService.getByProfileId(profile.getId());
            if (patientResult instanceof Result.Success<Patient, ApplicationError>) {
                patientId = patientResult.toOptional().orElseThrow().getId().value().toString();
            }
        }

        String doctorId = null;
        if (user.getRole() == Roles.DOCTOR) {
            Result<Doctor, ApplicationError> doctorResult = doctorQueryService.getByProfileId(profile.getId());
            if (doctorResult instanceof Result.Success<Doctor, ApplicationError>) {
                doctorId = doctorResult.toOptional().orElseThrow().getId().value().toString();
            }
        }

        String token = tokenService.generateToken(
                user.getUsername().username(),
                user.getId(),
                user.getRole().name(),
                profileId,
                patientId,
                doctorId);

        return ResponseEntity.ok(new AuthenticatedUserResource(String.valueOf(user.getId()), token));
    }
}
