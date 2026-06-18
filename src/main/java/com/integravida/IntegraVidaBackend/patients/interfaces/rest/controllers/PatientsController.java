package com.integravida.IntegraVidaBackend.patients.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.ExternalProfileService;
import com.integravida.IntegraVidaBackend.patients.application.services.PatientCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.PatientQueryService;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreatePatientRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.PatientResource;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.UpdatePatientRequest;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientsController {
    private final PatientCommandService commandService;
    private final PatientQueryService queryService;
    private final ExternalProfileService externalProfileService;

    public PatientsController(PatientCommandService commandService,
                              PatientQueryService queryService,
                              ExternalProfileService externalProfileService) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.externalProfileService = externalProfileService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePatientRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(request.profileId(), request.medicalRecordNumber(), request.notes()),
                this::toResource,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<PatientResource> resources = queryService.getAll().stream().map(this::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(id),
                this::toResource,
                HttpStatus.OK);
    }

    @GetMapping("/by-profile/{profileId}")
    public ResponseEntity<?> getByProfileId(@PathVariable UUID profileId) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getByProfileId(profileId),
                this::toResource,
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody UpdatePatientRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.updateNotes(id, request.notes()),
                this::toResource,
                HttpStatus.OK);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.deactivate(id),
                this::toResource,
                HttpStatus.OK);
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<?> reactivate(@PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.reactivate(id),
                this::toResource,
                HttpStatus.OK);
    }

    private PatientResource toResource(com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient patient) {
        var fullName = externalProfileService.getFullNameByProfileId(patient.getProfileId()).orElse(null);
        var email = externalProfileService.getEmailByProfileId(patient.getProfileId()).orElse(null);
        return PatientResource.fromDomain(patient, fullName, email);
    }
}
