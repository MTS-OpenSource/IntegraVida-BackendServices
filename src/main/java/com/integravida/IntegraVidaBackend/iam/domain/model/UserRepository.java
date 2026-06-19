package com.integravida.IntegraVidaBackend.iam.domain.model;

import com.integravida.IntegraVidaBackend.iam.domain.model.valueobjects.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(Username username);
    boolean existsByUsername(Username username);
}
