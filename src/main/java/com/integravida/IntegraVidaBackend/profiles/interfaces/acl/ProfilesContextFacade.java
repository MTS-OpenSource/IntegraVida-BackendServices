package com.integravida.IntegraVidaBackend.profiles.interfaces.acl;

import com.integravida.IntegraVidaBackend.profiles.application.services.ProfileQueryService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * ProfilesContextFacade — ACL
 * Exposes a stable API of the Profiles bounded context
 * consumed by patients and doctors bounded contexts.
 */
@Service
public class ProfilesContextFacade {
    private final ProfileQueryService queryService;

    public ProfilesContextFacade(ProfileQueryService queryService) {
        this.queryService = queryService;
    }

    public boolean existsProfile(UUID profileId) {
        return queryService.getById(profileId).isSuccess();
    }

    public Optional<String> getFullNameByProfileId(UUID profileId) {
        return queryService.getById(profileId)
                .toOptional().map(p -> p.getName().fullName());
    }

    public Optional<String> getEmailByProfileId(UUID profileId) {
        return queryService.getById(profileId)
                .toOptional().map(p -> p.getEmail().value());
    }
}
