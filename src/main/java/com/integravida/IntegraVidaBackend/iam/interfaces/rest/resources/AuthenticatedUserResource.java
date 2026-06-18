package com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(
        String username,
        String token
) {}