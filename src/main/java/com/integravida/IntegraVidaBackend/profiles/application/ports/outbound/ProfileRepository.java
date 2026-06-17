package com.integravida.IntegraVidaBackend.profiles.application.ports.outbound;

import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.EmailAddress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository {
    Profile save(Profile profile);
    Optional<Profile> findById(UUID id);
    Optional<Profile> findByEmail(EmailAddress email);
    boolean existsByEmail(EmailAddress email);
    List<Profile> findAll();
}
