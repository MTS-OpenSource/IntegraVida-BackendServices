package com.integravida.IntegraVidaBackend.patients.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.patients.application.services.MedicationCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.MedicationQueryService;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.MedicationSchedule;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreateMedicationRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.MedicationResource;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medications")
public class MedicationsController {
    private final MedicationCommandService commandService;
    private final MedicationQueryService queryService;

    public MedicationsController(MedicationCommandService commandService,
                                 MedicationQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateMedicationRequest request) {
        var schedule = MedicationSchedule.of(request.daysOfWeek(), request.doseTimes(), request.instructions());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.create(request.patientId(), request.treatmentId(), request.name(), request.dosage(), schedule),
                MedicationResource::fromDomain,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) UUID patientId,
                                    @RequestParam(required = false) UUID treatmentId) {
        if (patientId != null) {
            List<MedicationResource> resources = queryService.getByPatientId(patientId).stream().map(MedicationResource::fromDomain).toList();
            return ResponseEntity.ok(resources);
        }
        if (treatmentId != null) {
            List<MedicationResource> resources = queryService.getByTreatmentId(treatmentId).stream().map(MedicationResource::fromDomain).toList();
            return ResponseEntity.ok(resources);
        }
        List<MedicationResource> resources = queryService.getAll().stream().map(MedicationResource::fromDomain).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                queryService.getById(id),
                MedicationResource::fromDomain,
                HttpStatus.OK);
    }
}
