package com.integravida.IntegraVidaBackend.profiles.infrastructure.persistence.jpa.repositories;

import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByEmail_Email(String email);
    boolean existsByEmail_Email(String email);
}
