package com.integravida.IntegraVidaBackend.patients.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.patients.application.services.MedicationIntakeCommandService;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreateMedicationIntakeRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.MedicationIntakeResource;
import com.integravida.IntegraVidaBackend.shared.interfaces.rest.transform.ResponseEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/medication-intakes")
public class MedicationIntakesController {
    private final MedicationIntakeCommandService commandService;

    public MedicationIntakesController(MedicationIntakeCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateMedicationIntakeRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.register(request.medicationId(), request.patientId(), request.takenAt(), request.notes()),
                MedicationIntakeResource::fromDomain,
                HttpStatus.CREATED);
    }
}
