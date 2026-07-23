package com.integravida.IntegraVidaBackend.iam.interfaces.rest;

import com.integravida.IntegraVidaBackend.iam.application.services.GoogleOAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/google")
@Tag(name = "Google OAuth2", description = "Google OAuth2 authentication endpoints")
public class GoogleOAuth2CallbackController {

    private final GoogleOAuth2Service googleOAuth2Service;

    public GoogleOAuth2CallbackController(GoogleOAuth2Service googleOAuth2Service) {
        this.googleOAuth2Service = googleOAuth2Service;
    }

    @Operation(
            summary = "Google login callback",
            description = "Receives Google user info from the frontend and returns a JWT token. " +
                    "The frontend should use angular-oauth2-oidc or similar library to authenticate with Google, " +
                    "then send the user info to this endpoint."
    )
    @PostMapping("/login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> googleUserInfo) {
        String email = googleUserInfo.get("email");
        String firstName = googleUserInfo.get("firstName");
        String lastName = googleUserInfo.get("lastName");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }

        if (firstName == null || firstName.isBlank()) {
            firstName = "";
        }

        if (lastName == null || lastName.isBlank()) {
            lastName = "";
        }

        try {
            Map<String, String> result = googleOAuth2Service.handleGoogleLogin(email, firstName, lastName);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to process Google login: " + e.getMessage()));
        }
    }
}
