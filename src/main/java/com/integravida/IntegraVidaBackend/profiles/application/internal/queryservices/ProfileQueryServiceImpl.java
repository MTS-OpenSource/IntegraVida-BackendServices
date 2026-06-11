package com.integravida.IntegraVidaBackend.profiles.application.internal.queryservices;

import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.profiles.domain.services.ProfileQueryService;
import com.integravida.IntegraVidaBackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;

    @Override
    public Optional<Profile> handle(Long profileId) {
        return profileRepository.findById(profileId);
    }

    @Override
    public Optional<Profile> handleByEmail(String email) {
        return profileRepository.findByEmail_Email(email);
    }

    @Override
    public List<Profile> handleAll() {
        return profileRepository.findAll();
    }
}
