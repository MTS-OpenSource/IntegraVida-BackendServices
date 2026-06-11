package com.integravida.IntegraVidaBackend.profiles.application.internal.commandservices;

import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.profiles.domain.services.ProfileCommandService;
import com.integravida.IntegraVidaBackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.CreateProfileResource;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.UpdateProfileResource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.integravida.IntegraVidaBackend.profiles.domain.model.events.ProfileCreatedEvent;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final ProfileRepository profileRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Profile handle(CreateProfileResource resource) {
        if (profileRepository.existsByEmail_Email(resource.email())) {
            throw new IllegalArgumentException("A profile with email " + resource.email() + " already exists");
        }

        var profile = new Profile(
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.phoneNumber(),
                resource.dateOfBirth()
        );

        var saved = profileRepository.save(profile);

        // Publish domain event — consumed by patients and doctors contexts
        eventPublisher.publishEvent(new ProfileCreatedEvent(
                saved.getId(),
                saved.getEmailAddress(),
                saved.getFullName()
        ));

        return saved;
    }

    @Override
    public Optional<Profile> handle(Long profileId, UpdateProfileResource resource) {
        return profileRepository.findById(profileId).map(profile -> {
            if (resource.firstName() != null && resource.lastName() != null) {
                profile.updateName(resource.firstName(), resource.lastName());
            }
            if (resource.phoneNumber() != null) {
                profile.updatePhoneNumber(resource.phoneNumber());
            }
            if (resource.dateOfBirth() != null) {
                profile.updateDateOfBirth(resource.dateOfBirth());
            }
            return profileRepository.save(profile);
        });
    }
}
