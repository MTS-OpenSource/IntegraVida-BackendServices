package com.integravida.IntegraVidaBackend.iam.interfaces.rest;

import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.SignInResource;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.SignUpResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
@Tag(name = "Authentication", description = "Endpoints de autenticación y registro de usuarios")
public class AuthenticationController {

    /* * TODO: Descomenta e inyecta tus servicios reales aquí cuando verifiques
     * que coinciden con los nombres de la T03.
     * * private final UserCommandService userCommandService;
     * private final UserQueryService userQueryService;
     *
     * public AuthenticationController(UserCommandService userCommandService, UserQueryService userQueryService) {
     * this.userCommandService = userCommandService;
     * this.userQueryService = userQueryService;
     * }
     */

    @Operation(summary = "Registrar un nuevo usuario (Sign-Up)", description = "Crea una cuenta para un Patient o Doctor.")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpResource resource) {
        try {
            return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar el usuario");
        }
    }

    @Operation(summary = "Iniciar sesión (Sign-In)", description = "Autentica al usuario y devuelve un token JWT.")
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(@Valid @RequestBody SignInResource resource) {
        try {
            String generatedToken = "jwt-token-simulado-aqui";

            AuthenticatedUserResource response = new AuthenticatedUserResource(resource.username(), generatedToken);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}