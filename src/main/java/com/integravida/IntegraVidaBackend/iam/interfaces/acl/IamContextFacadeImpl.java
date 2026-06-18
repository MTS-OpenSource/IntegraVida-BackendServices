package com.integravida.IntegraVidaBackend.iam.interfaces.acl;

import com.integravida.IntegraVidaBackend.iam.application.internal.outboundservices.tokens.TokenService;
import org.springframework.stereotype.Service;

@Service
public class IamContextFacadeImpl implements IamContextFacade {

    private final TokenService tokenService;

    public IamContextFacadeImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Long fetchUserIdByUsername(String username) {
        return 1L;
    }

    @Override
    public String fetchUsernameFromToken(String token) {
        return tokenService.getUsernameFromToken(token);
    }

    @Override
    public boolean isTokenValid(String token) {
        return tokenService.validateToken(token);
    }
}