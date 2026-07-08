package com.integravida.IntegraVidaBackend.profiles.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.profiles.application.services.ProfileCommandService;
import com.integravida.IntegraVidaBackend.profiles.application.services.ProfileQueryService;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.CreateProfileRequest;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.ProfileResource;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.UpdateProfileRequest;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Profiles", description = "User profile management endpoints")
@RestController
@RequestMapping("/api/v1/profiles")
public class ProfilesController {
    private final ProfileCommandService commandService;
    private final ProfileQueryService   queryService;
    private final JwtClaimsExtractor    jwtClaimsExtractor;

    public ProfilesController(ProfileCommandService commandService,
                              ProfileQueryService queryService,
                              JwtClaimsExtractor jwtClaimsExtractor) {
        this.commandService = commandService;
        this.queryService   = queryService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Operation(summary = "Create a profile", description = "Creates a new user profile and publishes ProfileCreatedEvent.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Profile created",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProfileResource.class),
                examples = @ExampleObject(value = """
                    {
                      "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
                      "firstName": "Ana",
                      "lastName": "García",
                      "fullName": "Ana García",
                      "email": "ana@glucosmart.com",
                      "phoneNumber": "+51 987 654 321",
                      "dateOfBirth": "1990-03-15",
                      "createdAt": "2026-06-16T10:00:00",
                      "updatedAt": "2026-06-16T10:00:00"
                    }
                    """))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Profile payload", required = true,
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateProfileRequest.class),
                    examples = @ExampleObject(value = """
                        {
                          "firstName": "Ana",
                          "lastName": "García",
                          "email": "ana@glucosmart.com",
                          "phoneNumber": "+51 987 654 321",
                          "dateOfBirth": "1990-03-15"
                        }
                        """)))
            @RequestBody CreateProfileRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(request.firstName(), request.lastName(),
                        request.email(), request.phoneNumber(), request.dateOfBirth()),
                ProfileResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get all profiles or search by email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profiles found",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ProfileResource.class))))
    })
    @GetMapping
    public ResponseEntity<?> getProfiles(
            @Parameter(description = "Filter by email") @RequestParam(required = false) String email) {
        if (email != null) {
            return ResponseEntityAssembler.toResponseEntityFromResult(
                    queryService.getByEmail(email),
                    ProfileResource::fromDomain,
                    HttpStatus.OK);
        }
        List<ProfileResource> profiles = queryService.getAll()
                .stream().map(ProfileResource::fromDomain).toList();
        return ResponseEntity.ok(profiles);
    }

    @Operation(summary = "Get my profile", description = "Returns the profile of the authenticated user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile found",
            content = @Content(schema = @Schema(implementation = ProfileResource.class))),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        var profileId = java.util.UUID.fromString(jwtClaimsExtractor.extractProfileId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(profileId),
                ProfileResource::fromDomain,
                HttpStatus.OK);
    }

    @Operation(summary = "Update my profile", description = "Updates the profile of the authenticated user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile updated"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        var profileId = java.util.UUID.fromString(jwtClaimsExtractor.extractProfileId());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.update(profileId, request.firstName(), request.lastName(),
                        request.phoneNumber(), request.dateOfBirth()),
                ProfileResource::fromDomain,
                HttpStatus.OK);
    }
}
