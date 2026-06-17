package com.integravida.IntegraVidaBackend.profiles.application.services;

import com.integravida.IntegraVidaBackend.profiles.application.ports.outbound.ProfileRepository;
import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.DateOfBirth;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.EmailAddress;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.PersonName;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.PhoneNumber;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class ProfileCommandService {
    private final ProfileRepository profileRepository;

    public ProfileCommandService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Result<Profile, ApplicationError> create(String firstName, String lastName,
                                                    String email, String phoneNumber,
                                                    LocalDate dateOfBirth) {
        var emailVO = EmailAddress.of(email);
        if (profileRepository.existsByEmail(emailVO)) {
            return Result.failure(ApplicationError.conflict("profile", "email already registered: " + email));
        }
        var profile = Profile.create(
                UUID.randomUUID(),
                PersonName.of(firstName, lastName),
                emailVO,
                PhoneNumber.of(phoneNumber),
                DateOfBirth.of(dateOfBirth),
                LocalDateTime.now());
        var saved = profileRepository.save(profile);
        saved.clearDomainEvents();
        return Result.success(saved);
    }

    public Result<Profile, ApplicationError> update(UUID id, String firstName, String lastName,
                                                    String phoneNumber, LocalDate dateOfBirth) {
        return profileRepository.findById(id)
                .map(profile -> {
                    profile.update(
                            PersonName.of(firstName, lastName),
                            PhoneNumber.of(phoneNumber),
                            DateOfBirth.of(dateOfBirth),
                            LocalDateTime.now());
                    return Result.<Profile, ApplicationError>success(profileRepository.save(profile));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("profile", id.toString())));
    }
}
