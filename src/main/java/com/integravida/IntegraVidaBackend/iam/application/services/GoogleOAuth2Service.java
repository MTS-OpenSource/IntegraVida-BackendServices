package com.integravida.IntegraVidaBackend.iam.application.services;

import com.integravida.IntegraVidaBackend.iam.application.internal.commandservices.UserCommandServiceImpl;
import com.integravida.IntegraVidaBackend.iam.domain.model.Roles;
import com.integravida.IntegraVidaBackend.iam.domain.model.User;
import com.integravida.IntegraVidaBackend.iam.domain.model.UserRepository;
import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.TokenService;
import com.integravida.IntegraVidaBackend.patients.application.services.PatientCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.PatientQueryService;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient;
import com.integravida.IntegraVidaBackend.profiles.application.services.ProfileCommandService;
import com.integravida.IntegraVidaBackend.profiles.application.services.ProfileQueryService;
import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class GoogleOAuth2Service {

    private final UserRepository userRepository;
    private final UserCommandServiceImpl userCommandService;
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;
    private final PatientCommandService patientCommandService;
    private final PatientQueryService patientQueryService;
    private final TokenService tokenService;

    public GoogleOAuth2Service(UserRepository userRepository,
                               UserCommandServiceImpl userCommandService,
                               ProfileCommandService profileCommandService,
                               ProfileQueryService profileQueryService,
                               PatientCommandService patientCommandService,
                               PatientQueryService patientQueryService,
                               TokenService tokenService) {
        this.userRepository = userRepository;
        this.userCommandService = userCommandService;
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
        this.patientCommandService = patientCommandService;
        this.patientQueryService = patientQueryService;
        this.tokenService = tokenService;
    }

    public Map<String, String> handleGoogleLogin(String email, String firstName, String lastName) {
        var existingUser = userRepository.findByEmail(email);

        User user;
        String profileId;
        String patientId = null;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            var profileResult = profileQueryService.getByEmail(user.getEmail());
            if (profileResult instanceof Result.Success<Profile, ApplicationError> success) {
                profileId = success.value().getId().toString();
                if (user.getRole() == Roles.PATIENT) {
                    var patientResult = patientQueryService.getByProfileId(success.value().getId());
                    if (patientResult instanceof Result.Success<Patient, ApplicationError> patientSuccess) {
                        patientId = patientSuccess.value().getId().value().toString();
                    }
                }
            } else {
                throw new RuntimeException("Profile not found for existing Google user: " + email);
            }
        } else {
            String googleUsername = email.split("@")[0];
            String encodedPassword = UUID.randomUUID().toString();

            Result<User, ApplicationError> userResult = userCommandService.signUp(
                    googleUsername, encodedPassword, email, Roles.PATIENT);

            if (userResult instanceof Result.Failure<User, ApplicationError> failure) {
                throw new RuntimeException("Failed to create user: " + failure.error().message());
            }

            user = userResult.toOptional().orElseThrow();

            String phoneNumber = "0000000000";
            LocalDate dateOfBirth = LocalDate.now().minusYears(25);

            Result<Profile, ApplicationError> profileResult = profileCommandService.create(
                    firstName, lastName, email, phoneNumber, dateOfBirth);

            if (profileResult instanceof Result.Failure<Profile, ApplicationError> failure) {
                throw new RuntimeException("Failed to create profile: " + failure.error().message());
            }

            Profile profile = profileResult.toOptional().orElseThrow();
            profileId = profile.getId().toString();

            String medicalRecordNumber = "MRN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Result<Patient, ApplicationError> patientResult = patientCommandService.create(
                    profile.getId(), medicalRecordNumber, "Patient created via Google OAuth2");

            if (patientResult instanceof Result.Success<Patient, ApplicationError> success) {
                patientId = success.value().getId().value().toString();
            }
        }

        String token = tokenService.generateToken(
                user.getUsername().username(),
                user.getId(),
                user.getRole().name(),
                profileId,
                patientId,
                null);

        return Map.of(
                "userId", String.valueOf(user.getId()),
                "token", token,
                "email", email,
                "role", user.getRole().name());
    }
}
