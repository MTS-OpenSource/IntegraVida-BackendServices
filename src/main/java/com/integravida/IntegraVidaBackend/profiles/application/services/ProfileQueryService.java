package com.integravida.IntegraVidaBackend.profiles.application.services;

import com.integravida.IntegraVidaBackend.profiles.application.ports.outbound.ProfileRepository;
import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.EmailAddress;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProfileQueryService {
    private final ProfileRepository profileRepository;

    public ProfileQueryService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Result<Profile, ApplicationError> getById(UUID id) {
        return profileRepository.findById(id)
                .map(Result::<Profile, ApplicationError>success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("profile", id.toString())));
    }

    public Result<Profile, ApplicationError> getByEmail(String email) {
        return profileRepository.findByEmail(EmailAddress.of(email))
                .map(Result::<Profile, ApplicationError>success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("profile", email)));
    }

    public List<Profile> getAll() {
        return profileRepository.findAll();
    }
}
