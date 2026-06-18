package com.integravida.IntegraVidaBackend.patients.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.patients.application.services.TreatmentCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.TreatmentQueryService;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreateTreatmentRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.TreatmentResource;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.UpdateTreatmentRequest;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/treatments")
public class TreatmentsController {
    private final TreatmentCommandService commandService;
    private final TreatmentQueryService queryService;

    public TreatmentsController(TreatmentCommandService commandService,
                                TreatmentQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateTreatmentRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(request.patientId(), request.name(), request.description(), request.startDate(), request.endDate()),
                TreatmentResource::fromDomain,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) UUID patientId) {
        if (patientId != null) {
            List<TreatmentResource> resources = queryService.getByPatientId(patientId).stream().map(TreatmentResource::fromDomain).toList();
            return ResponseEntity.ok(resources);
        }
        List<TreatmentResource> resources = queryService.getAll().stream().map(TreatmentResource::fromDomain).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(id),
                TreatmentResource::fromDomain,
                HttpStatus.OK);
    }

    @GetMapping("/active/{patientId}")
    public ResponseEntity<?> getActiveByPatientId(@PathVariable UUID patientId) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getActiveByPatientId(patientId),
                TreatmentResource::fromDomain,
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody UpdateTreatmentRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.update(id, request.name(), request.description(), request.startDate(), request.endDate(), request.status()),
                TreatmentResource::fromDomain,
                HttpStatus.OK);
    }
}
