package com.integravida.IntegraVidaBackend.iam.application.internal.queryservices;

import com.integravida.IntegraVidaBackend.iam.domain.model.User;
import com.integravida.IntegraVidaBackend.iam.domain.model.UserRepository;
import com.integravida.IntegraVidaBackend.iam.domain.model.valueobjects.Username;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl {
    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Result<User, ApplicationError> getByUsername(String rawUsername) {
        var username = new Username(rawUsername);
        return userRepository.findByUsername(username)
                .map(Result::<User, ApplicationError>success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("user", rawUsername)));
    }
}
