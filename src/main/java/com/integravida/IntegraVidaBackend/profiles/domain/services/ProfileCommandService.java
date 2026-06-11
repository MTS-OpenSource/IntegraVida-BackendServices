package com.integravida.IntegraVidaBackend.profiles.domain.services;

import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.CreateProfileResource;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.UpdateProfileResource;

import java.util.Optional;

public interface ProfileCommandService {
    Profile handle(CreateProfileResource resource);
    Optional<Profile> handle(Long profileId, UpdateProfileResource resource);
}
