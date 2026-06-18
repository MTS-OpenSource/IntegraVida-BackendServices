package com.integravida.IntegraVidaBackend.iam.interfaces.acl;


public interface IamContextFacade {

    Long fetchUserIdByUsername(String username);

    String fetchUsernameFromToken(String token);

    boolean isTokenValid(String token);
}