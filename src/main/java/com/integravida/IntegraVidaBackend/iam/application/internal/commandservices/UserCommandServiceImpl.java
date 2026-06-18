package com.integravida.IntegraVidaBackend.iam.application.internal.commandservices;

import com.integravida.IntegraVidaBackend.iam.domain.model.Roles;
import com.integravida.IntegraVidaBackend.iam.domain.model.User;
import com.integravida.IntegraVidaBackend.iam.domain.model.UserRepository;
import com.integravida.IntegraVidaBackend.iam.domain.model.valueobjects.Username;
import com.integravida.IntegraVidaBackend.iam.infrastructure.hashing.DefaultPasswordEncoder;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCommandServiceImpl {

    private final UserRepository userRepository;
    private final DefaultPasswordEncoder passwordEncoder;

    public UserCommandServiceImpl(UserRepository userRepository, DefaultPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Result<User, ApplicationError> signUp(String rawUsername, String rawPassword, Roles role) {
        var usernameVO = new Username(rawUsername);

        if (userRepository.existsByUsername(usernameVO)) {
            return Result.failure(ApplicationError.conflict("user", "username already exists: " + rawUsername));
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user;
        if (role == Roles.PATIENT) {
            user = User.createPatient(rawUsername, encodedPassword);
        } else if (role == Roles.DOCTOR) {
            user = User.createDoctor(rawUsername, encodedPassword);
        } else {
            return Result.failure(ApplicationError.validationError("role", "invalid user role"));
        }

        var savedUser = userRepository.save(user);
        savedUser.clearDomainEvents();

        return Result.success(savedUser);
    }
}
