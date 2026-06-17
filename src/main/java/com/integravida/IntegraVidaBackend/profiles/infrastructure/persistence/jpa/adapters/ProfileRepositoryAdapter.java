package com.integravida.IntegraVidaBackend.profiles.infrastructure.persistence.jpa.adapters;

import com.integravida.IntegraVidaBackend.profiles.application.ports.outbound.ProfileRepository;
import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.EmailAddress;
import com.integravida.IntegraVidaBackend.profiles.infrastructure.persistence.jpa.mappers.ProfileJpaMapper;
import com.integravida.IntegraVidaBackend.profiles.infrastructure.persistence.jpa.repositories.ProfileJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfileRepositoryAdapter implements ProfileRepository {
    private final ProfileJpaRepository jpa;

    public ProfileRepositoryAdapter(ProfileJpaRepository jpa) { this.jpa = jpa; }

    @Override
    public Profile save(Profile profile) {
        jpa.save(ProfileJpaMapper.toEntity(profile));
        return profile;
    }

    @Override
    public Optional<Profile> findById(UUID id) {
        return jpa.findById(id).map(ProfileJpaMapper::toDomain);
    }

    @Override
    public Optional<Profile> findByEmail(EmailAddress email) {
        return jpa.findByEmail(email.value()).map(ProfileJpaMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(EmailAddress email) {
        return jpa.existsByEmail(email.value());
    }

    @Override
    public List<Profile> findAll() {
        return jpa.findAll().stream().map(ProfileJpaMapper::toDomain).toList();
    }
}
