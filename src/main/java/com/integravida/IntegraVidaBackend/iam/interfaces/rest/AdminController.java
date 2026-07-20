package com.integravida.IntegraVidaBackend.iam.interfaces.rest;

import com.integravida.IntegraVidaBackend.iam.application.internal.commandservices.UserCommandServiceImpl;
import com.integravida.IntegraVidaBackend.iam.application.internal.queryservices.UserQueryServiceImpl;
import com.integravida.IntegraVidaBackend.iam.domain.model.Roles;
import com.integravida.IntegraVidaBackend.iam.domain.model.User;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.AdminCreateAdminRequest;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.AdminCreateDoctorRequest;
import com.integravida.IntegraVidaBackend.iam.interfaces.rest.resources.UserResource;
import com.integravida.IntegraVidaBackend.patients.application.services.DoctorCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.DoctorQueryService;
import com.integravida.IntegraVidaBackend.patients.application.services.PatientQueryService;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Doctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.PatientDoctor;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientDoctorRepository;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.AdminAssignPatientRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.DoctorResource;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.PatientAssignmentResource;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.PatientResource;
import com.integravida.IntegraVidaBackend.profiles.application.services.ProfileCommandService;
import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "Admin-only endpoints for global management")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserQueryServiceImpl userQueryService;
    private final UserCommandServiceImpl userCommandService;
    private final ProfileCommandService profileCommandService;
    private final DoctorCommandService doctorCommandService;
    private final DoctorQueryService doctorQueryService;
    private final PatientQueryService patientQueryService;
    private final PatientDoctorRepository patientDoctorRepository;

    public AdminController(
            UserQueryServiceImpl userQueryService,
            UserCommandServiceImpl userCommandService,
            ProfileCommandService profileCommandService,
            DoctorCommandService doctorCommandService,
            DoctorQueryService doctorQueryService,
            PatientQueryService patientQueryService,
            PatientDoctorRepository patientDoctorRepository) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
        this.profileCommandService = profileCommandService;
        this.doctorCommandService = doctorCommandService;
        this.doctorQueryService = doctorQueryService;
        this.patientQueryService = patientQueryService;
        this.patientDoctorRepository = patientDoctorRepository;
    }

    @Operation(summary = "List all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResource.class))))
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserResource>> getAllUsers() {
        List<UserResource> resources = userQueryService.getAll()
                .stream().map(UserResource::fromDomain).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "List all doctors")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctors found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DoctorResource.class))))
    })
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResource>> getAllDoctors() {
        List<DoctorResource> resources = doctorQueryService.getAllDoctors()
                .stream().map(DoctorResource::fromDomain).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "List all patients")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patients found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientResource.class))))
    })
    @GetMapping("/patients")
    public ResponseEntity<List<Map<String, Object>>> getAllPatients() {
        List<Map<String, Object>> resources = patientQueryService.getAll()
                .stream().<Map<String, Object>>map(patient -> {
                    var map = new HashMap<String, Object>();
                    map.put("id", patient.getId().value());
                    map.put("profileId", patient.getProfileId());
                    map.put("medicalRecordNumber", patient.getMedicalRecordNumber());
                    map.put("notes", patient.getNotes());
                    map.put("active", patient.isActive());
                    map.put("createdAt", patient.getCreatedAt());
                    map.put("updatedAt", patient.getUpdatedAt());
                    return map;
                }).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Create a doctor user with profile and doctor record")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Doctor created"),
            @ApiResponse(responseCode = "409", description = "Username already exists or profile already linked")
    })
    @PostMapping("/doctors")
    public ResponseEntity<?> createDoctor(@Valid @RequestBody AdminCreateDoctorRequest request) {
        Result<User, ApplicationError> userResult = userCommandService.signUp(
                request.username(), request.password(), request.email(), Roles.DOCTOR);

        if (userResult instanceof Result.Failure<User, ApplicationError> failure) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(failure.error().message());
        }

        User user = userResult.toOptional().orElseThrow();

        Result<Profile, ApplicationError> profileResult = profileCommandService.create(
                request.firstName(), request.lastName(), request.email(),
                request.phoneNumber(), request.dateOfBirth());

        if (profileResult instanceof Result.Failure<Profile, ApplicationError> failure) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(failure.error().message());
        }

        Profile profile = profileResult.toOptional().orElseThrow();

        Result<Doctor, ApplicationError> doctorResult = doctorCommandService.create(
                profile.getId(), request.doctorRecordNumber(), request.doctorNotes());

        if (doctorResult instanceof Result.Failure<Doctor, ApplicationError> failure) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(failure.error().message());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DoctorResource.fromDomain(doctorResult.toOptional().orElseThrow()));
    }

    @Operation(summary = "Create an admin user with profile")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Admin created"),
            @ApiResponse(responseCode = "409", description = "Username already exists")
    })
    @PostMapping("/admins")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminCreateAdminRequest request) {
        Result<User, ApplicationError> userResult = userCommandService.signUp(
                request.username(), request.password(), request.email(), Roles.ADMIN);

        if (userResult instanceof Result.Failure<User, ApplicationError> failure) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(failure.error().message());
        }

        Result<Profile, ApplicationError> profileResult = profileCommandService.create(
                request.firstName(), request.lastName(), request.email(),
                request.phoneNumber(), request.dateOfBirth());

        if (profileResult instanceof Result.Failure<Profile, ApplicationError> failure) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(failure.error().message());
        }

        UserResource resource = UserResource.fromDomain(userResult.toOptional().orElseThrow());
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @Operation(summary = "Assign a patient to a doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Assignment created"),
            @ApiResponse(responseCode = "404", description = "Patient or doctor not found"),
            @ApiResponse(responseCode = "409", description = "Patient already assigned to this doctor")
    })
    @PostMapping("/assignments")
    public ResponseEntity<?> createAssignment(@Valid @RequestBody AdminAssignPatientRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                doctorCommandService.assignPatient(request.patientId(), request.doctorId()),
                PatientAssignmentResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a patient-doctor assignment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Assignment deleted"),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable UUID id) {
        var existing = patientDoctorRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found: " + id);
        }
        patientDoctorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Dashboard with global stats")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stats returned")
    })
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        var stats = new HashMap<String, Object>();
        stats.put("totalUsers", userQueryService.getAll().size());
        stats.put("totalPatients", patientQueryService.getAll().size());
        stats.put("totalDoctors", doctorQueryService.getAllDoctors().size());
        return ResponseEntity.ok(stats);
    }
}
