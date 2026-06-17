package com.integravida.IntegraVidaBackend.profiles.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.profiles.infrastructure.persistence.jpa.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileJpaRepository extends JpaRepository<ProfileEntity, UUID> {
    Optional<ProfileEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
