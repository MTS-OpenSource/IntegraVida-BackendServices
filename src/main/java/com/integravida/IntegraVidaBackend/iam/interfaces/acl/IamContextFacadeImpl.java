package com.integravida.IntegraVidaBackend.iam.interfaces.acl;

import org.springframework.stereotype.Service;

@Service
public class IamContextFacadeImpl implements IamContextFacade {

    @Override
    public Long fetchUserIdByUsername(String username) {
        return 1L;
    }

    @Override
    public String fetchUsernameFromToken(String token) {
        return null;
    }

    @Override
    public boolean isTokenValid(String token) {
        return true;
    }
}
