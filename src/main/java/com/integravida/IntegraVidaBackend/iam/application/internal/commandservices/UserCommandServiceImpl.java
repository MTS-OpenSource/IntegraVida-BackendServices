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

import java.util.Optional;

@Service
@Transactional
public class UserCommandServiceImpl {

    private final UserRepository userRepository;
    private final DefaultPasswordEncoder passwordEncoder;

    public UserCommandServiceImpl(UserRepository userRepository, DefaultPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Result<User, ApplicationError> signUp(String rawUsername, String rawPassword, String email, Roles role) {
        var usernameVO = new Username(rawUsername);

        if (userRepository.existsByUsername(usernameVO)) {
            return Result.failure(ApplicationError.conflict("user", "username already exists: " + rawUsername));
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user;
        if (role == Roles.PATIENT) {
            user = User.createPatient(rawUsername, encodedPassword, email);
        } else if (role == Roles.DOCTOR) {
            user = User.createDoctor(rawUsername, encodedPassword, email);
        } else if (role == Roles.ADMIN) {
            user = User.createAdmin(rawUsername, encodedPassword, email);
        } else {
            return Result.failure(ApplicationError.validationError("role", "invalid user role"));
        }

        var savedUser = userRepository.save(user);
        savedUser.clearDomainEvents();

        return Result.success(savedUser);
    }

    public Result<User, ApplicationError> verifyCredentials(String rawUsername, String rawPassword) {
        var usernameVO = new Username(rawUsername);
        Optional<User> userOptional = userRepository.findByUsername(usernameVO);

        if (userOptional.isEmpty()) {
            return Result.failure(ApplicationError.notFound("user", "User not found"));
        }

        User user = userOptional.get();

        String encryptedPassword = user.getPassword().password();

        if (!passwordEncoder.matches(rawPassword, encryptedPassword)) {
            return Result.failure(ApplicationError.validationError("credentials", "Invalid password"));
        }

        return Result.success(user);
    }
}
