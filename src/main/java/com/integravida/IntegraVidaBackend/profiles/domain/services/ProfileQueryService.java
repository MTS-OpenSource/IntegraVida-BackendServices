package com.integravida.IntegraVidaBackend.profiles.domain.services;

import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileQueryService {
    Optional<Profile> handle(Long profileId);
    Optional<Profile> handleByEmail(String email);
    List<Profile> handleAll();
}
