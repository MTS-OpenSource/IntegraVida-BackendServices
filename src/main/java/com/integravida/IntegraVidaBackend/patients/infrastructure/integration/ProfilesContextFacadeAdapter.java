package com.integravida.IntegraVidaBackend.patients.infrastructure.integration;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.ExternalProfileService;
import com.integravida.IntegraVidaBackend.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfilesContextFacadeAdapter implements ExternalProfileService {
    private final ProfilesContextFacade profilesContextFacade;

    public ProfilesContextFacadeAdapter(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    @Override
    public boolean existsById(UUID profileId) {
        return profilesContextFacade.existsProfile(profileId);
    }

    @Override
    public Optional<String> getFullNameByProfileId(UUID profileId) {
        return profilesContextFacade.getFullNameByProfileId(profileId);
    }

    @Override
    public Optional<String> getEmailByProfileId(UUID profileId) {
        return profilesContextFacade.getEmailByProfileId(profileId);
    }
}
