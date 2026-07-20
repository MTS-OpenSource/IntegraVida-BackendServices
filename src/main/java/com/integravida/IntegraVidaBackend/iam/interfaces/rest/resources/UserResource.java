package com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources;

import com.integravida.IntegraVidaBackend.iam.domain.model.Roles;
import com.integravida.IntegraVidaBackend.iam.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "UserResource", description = "Public user information for admin listing")
public record UserResource(
        Long id,
        String username,
        String email,
        Roles role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static UserResource fromDomain(User user) {
        return new UserResource(
                user.getId(),
                user.getUsername().username(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
