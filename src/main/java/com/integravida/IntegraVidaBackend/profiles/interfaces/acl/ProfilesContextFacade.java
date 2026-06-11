package com.integravida.IntegraVidaBackend.profiles.interfaces.acl;

import com.integravida.IntegraVidaBackend.profiles.domain.services.ProfileQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ProfilesContextFacade — Anti-Corruption Layer
 *
 * This facade exposes a simplified API of the Profiles bounded context
 * to be consumed by other bounded contexts (patients, doctors, monitoring, etc.).
 * It prevents direct coupling between contexts by providing a stable interface.
 */
@Service
@RequiredArgsConstructor
public class ProfilesContextFacade {

    private final ProfileQueryService profileQueryService;

    /**
     * Checks whether a profile exists for the given profile ID.
     * Used by the patients context to validate before creating a Patient.
     */
    public boolean existsProfile(Long profileId) {
        return profileQueryService.handle(profileId).isPresent();
    }

    /**
     * Returns the full name of a profile by its ID.
     * Returns empty if the profile does not exist.
     */
    public Optional<String> getFullNameByProfileId(Long profileId) {
        return profileQueryService.handle(profileId)
                .map(p -> p.getName().getFullName());
    }

    /**
     * Returns the email of a profile by its ID.
     */
    public Optional<String> getEmailByProfileId(Long profileId) {
        return profileQueryService.handle(profileId)
                .map(p -> p.getEmailAddress());
    }
}
