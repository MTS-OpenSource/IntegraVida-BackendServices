package com.integravida.IntegraVidaBackend.profiles.interfaces.rest;

import com.integravida.IntegraVidaBackend.profiles.domain.services.ProfileCommandService;
import com.integravida.IntegraVidaBackend.profiles.domain.services.ProfileQueryService;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.CreateProfileResource;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.ProfileResource;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.UpdateProfileResource;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@Tag(name = "Profiles", description = "Profile Management endpoints")
public class ProfilesController {

    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService   profileQueryService;

    // ── POST /api/v1/profiles ────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new profile")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Profile created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    public ResponseEntity<ProfileResource> createProfile(
            @Valid @RequestBody CreateProfileResource resource) {
        try {
            var profile = profileCommandService.handle(resource);
            var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(profileResource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // ── GET /api/v1/profiles ─────────────────────────────────

    @GetMapping
    @Operation(summary = "Get all profiles or search by email",
               description = "Returns all profiles. Optionally filter by email using ?email=value")
    public ResponseEntity<List<ProfileResource>> getProfiles(
            @RequestParam(required = false) String email) {

        if (email != null) {
            return profileQueryService.handleByEmail(email)
                    .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                    .map(r -> ResponseEntity.ok(List.of(r)))
                    .orElse(ResponseEntity.notFound().build());
        }

        var profiles = profileQueryService.handleAll()
                .stream()
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(profiles);
    }

    // ── GET /api/v1/profiles/{profileId} ─────────────────────

    @GetMapping("/{profileId}")
    @Operation(summary = "Get a profile by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile found"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<ProfileResource> getProfileById(@PathVariable Long profileId) {
        return profileQueryService.handle(profileId)
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── PUT /api/v1/profiles/{profileId} ─────────────────────

    @PutMapping("/{profileId}")
    @Operation(summary = "Update a profile's personal data")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<ProfileResource> updateProfile(
            @PathVariable Long profileId,
            @RequestBody UpdateProfileResource resource) {
        return profileCommandService.handle(profileId, resource)
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
