package com.integravida.IntegraVidaBackend.patients.interfaces.rest.controllers;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.monitoring.application.services.ClinicalObservationCommandService;
import com.integravida.IntegraVidaBackend.monitoring.application.services.ClinicalObservationQueryService;
import com.integravida.IntegraVidaBackend.monitoring.application.services.GlucoseRecordQueryService;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.ClinicalObservationResource;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.CreateClinicalObservationRequest;
import com.integravida.IntegraVidaBackend.monitoring.interfaces.rest.resources.GlucoseRecordResource;
import com.integravida.IntegraVidaBackend.medical.application.services.AppointmentCommandService;
import com.integravida.IntegraVidaBackend.medical.application.services.ClinicalReportCommandService;
import com.integravida.IntegraVidaBackend.medical.application.services.DiagnosisCommandService;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.AppointmentResource;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.CreateAppointmentRequest;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.ClinicalReportResource;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.CreateClinicalReportRequest;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.DiagnosisResource;
import com.integravida.IntegraVidaBackend.medical.interfaces.rest.resources.CreateDiagnosisRequest;
import com.integravida.IntegraVidaBackend.iam.application.services.OwnerShipService;
import com.integravida.IntegraVidaBackend.patients.application.services.DoctorQueryService;
import com.integravida.IntegraVidaBackend.patients.application.services.TreatmentCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.TreatmentQueryService;
import com.integravida.IntegraVidaBackend.patients.application.services.MedicationCommandService;
import com.integravida.IntegraVidaBackend.patients.application.services.MedicationQueryService;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.TreatmentResource;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreateTreatmentRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.MedicationResource;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.CreateMedicationRequest;
import com.integravida.IntegraVidaBackend.patients.interfaces.rest.resources.PatientAssignmentResource;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.MedicationSchedule;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors/me/patients")
@Tag(name = "Doctor - Patient Management", description = "Doctor-specific endpoints for managing assigned patients")
public class DoctorPatientController {

    private final DoctorQueryService doctorQueryService;
    private final OwnerShipService ownerShipService;
    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final TreatmentQueryService treatmentQueryService;
    private final TreatmentCommandService treatmentCommandService;
    private final MedicationQueryService medicationQueryService;
    private final MedicationCommandService medicationCommandService;
    private final GlucoseRecordQueryService glucoseRecordQueryService;
    private final ClinicalObservationQueryService clinicalObservationQueryService;
    private final ClinicalObservationCommandService clinicalObservationCommandService;
    private final DiagnosisCommandService diagnosisCommandService;
    private final ClinicalReportCommandService clinicalReportCommandService;
    private final AppointmentCommandService appointmentCommandService;

    public DoctorPatientController(
            DoctorQueryService doctorQueryService,
            OwnerShipService ownerShipService,
            JwtClaimsExtractor jwtClaimsExtractor,
            TreatmentQueryService treatmentQueryService,
            TreatmentCommandService treatmentCommandService,
            MedicationQueryService medicationQueryService,
            MedicationCommandService medicationCommandService,
            GlucoseRecordQueryService glucoseRecordQueryService,
            ClinicalObservationQueryService clinicalObservationQueryService,
            ClinicalObservationCommandService clinicalObservationCommandService,
            DiagnosisCommandService diagnosisCommandService,
            ClinicalReportCommandService clinicalReportCommandService,
            AppointmentCommandService appointmentCommandService) {
        this.doctorQueryService = doctorQueryService;
        this.ownerShipService = ownerShipService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
        this.treatmentQueryService = treatmentQueryService;
        this.treatmentCommandService = treatmentCommandService;
        this.medicationQueryService = medicationQueryService;
        this.medicationCommandService = medicationCommandService;
        this.glucoseRecordQueryService = glucoseRecordQueryService;
        this.clinicalObservationQueryService = clinicalObservationQueryService;
        this.clinicalObservationCommandService = clinicalObservationCommandService;
        this.diagnosisCommandService = diagnosisCommandService;
        this.clinicalReportCommandService = clinicalReportCommandService;
        this.appointmentCommandService = appointmentCommandService;
    }

    @Operation(summary = "List patients assigned to the authenticated doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patients found",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientAssignmentResource.class))))
    })
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping
    public ResponseEntity<?> getMyPatients() {
        UUID doctorId = UUID.fromString(jwtClaimsExtractor.extractDoctorId());
        List<PatientAssignmentResource> resources = doctorQueryService.getPatientsByDoctorId(doctorId)
                .stream().map(PatientAssignmentResource::fromDomain).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get patient summary for the authenticated doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient summary found"),
            @ApiResponse(responseCode = "403", description = "Patient not assigned to this doctor"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PreAuthorize("hasRole('DOCTOR') and @ownerShipService.isDoctorAssignedToPatient(#patientId)")
    @GetMapping("/{patientId}/summary")
    public ResponseEntity<?> getPatientSummary(@PathVariable UUID patientId) {
        var treatments = treatmentQueryService.getByPatientId(patientId);
        var medications = medicationQueryService.getByPatientId(patientId);
        var glucoseRecords = glucoseRecordQueryService.findByPatientId(PatientId.fromString(patientId.toString()));

        var summary = new java.util.HashMap<String, Object>();
        summary.put("patientId", patientId);
        summary.put("activeTreatments", treatments.stream().filter(t -> "ACTIVE".equals(t.getStatus())).count());
        summary.put("totalTreatments", treatments.size());
        summary.put("activeMedications", medications.stream().filter(m -> m.isActive()).count());
        summary.put("totalMedications", medications.size());
        summary.put("totalGlucoseRecords", glucoseRecords.size());

        return ResponseEntity.ok(summary);
    }

    @Operation(summary = "Get glucose records for a patient assigned to the doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Glucose records found"),
            @ApiResponse(responseCode = "403", description = "Patient not assigned to this doctor")
    })
    @PreAuthorize("hasRole('DOCTOR') and @ownerShipService.isDoctorAssignedToPatient(#patientId)")
    @GetMapping("/{patientId}/glucose-records")
    public ResponseEntity<?> getPatientGlucoseRecords(@PathVariable UUID patientId) {
        var patientIdObj = PatientId.fromString(patientId.toString());
        var records = glucoseRecordQueryService.findByPatientId(patientIdObj);
        return ResponseEntity.ok(records.stream().map(GlucoseRecordResource::fromDomain).toList());
    }

    @Operation(summary = "Get treatments for a patient assigned to the doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Treatments found"),
            @ApiResponse(responseCode = "403", description = "Patient not assigned to this doctor")
    })
    @PreAuthorize("hasRole('DOCTOR') and @ownerShipService.isDoctorAssignedToPatient(#patientId)")
    @GetMapping("/{patientId}/treatments")
    public ResponseEntity<?> getPatientTreatments(@PathVariable UUID patientId) {
        var treatments = treatmentQueryService.getByPatientId(patientId);
        return ResponseEntity.ok(treatments.stream().map(TreatmentResource::fromDomain).toList());
    }

    @Operation(summary = "Create a treatment for a patient assigned to the doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Treatment created"),
            @ApiResponse(responseCode = "403", description = "Patient not assigned to this doctor")
    })
    @PreAuthorize("hasRole('DOCTOR') and @ownerShipService.isDoctorAssignedToPatient(#patientId)")
    @PostMapping("/{patientId}/treatments")
    public ResponseEntity<?> createTreatmentForPatient(
            @PathVariable UUID patientId,
            @Valid @RequestBody CreateTreatmentRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                treatmentCommandService.create(patientId, request.name(), request.description(), request.startDate(), request.endDate()),
                TreatmentResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Create a medication for a patient assigned to the doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medication created"),
            @ApiResponse(responseCode = "403", description = "Patient not assigned to this doctor")
    })
    @PreAuthorize("hasRole('DOCTOR') and @ownerShipService.isDoctorAssignedToPatient(#patientId)")
    @PostMapping("/{patientId}/medications")
    public ResponseEntity<?> createMedicationForPatient(
            @PathVariable UUID patientId,
            @Valid @RequestBody CreateMedicationRequest request) {
        var schedule = MedicationSchedule.of(request.daysOfWeek(), request.doseTimes(), request.instructions());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                medicationCommandService.create(patientId, request.treatmentId(), request.name(), request.dosage(), schedule),
                MedicationResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Create a clinical observation for a patient assigned to the doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Clinical observation created"),
            @ApiResponse(responseCode = "403", description = "Patient not assigned to this doctor")
    })
    @PreAuthorize("hasRole('DOCTOR') and @ownerShipService.isDoctorAssignedToPatient(#patientId)")
    @PostMapping("/{patientId}/clinical-observations")
    public ResponseEntity<?> createClinicalObservationForPatient(
            @PathVariable UUID patientId,
            @Valid @RequestBody CreateClinicalObservationRequest request) {
        var patientIdObj = PatientId.fromString(patientId.toString());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                clinicalObservationCommandService.create(patientIdObj, request.category(), request.title(), request.content(), request.observedAt()),
                ClinicalObservationResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Create a diagnosis for a patient assigned to the doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Diagnosis created"),
            @ApiResponse(responseCode = "403", description = "Patient not assigned to this doctor")
    })
    @PreAuthorize("hasRole('DOCTOR') and @ownerShipService.isDoctorAssignedToPatient(#patientId)")
    @PostMapping("/{patientId}/diagnoses")
    public ResponseEntity<?> createDiagnosisForPatient(
            @PathVariable UUID patientId,
            @Valid @RequestBody CreateDiagnosisRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                diagnosisCommandService.create(request.description(), request.recommendation()),
                DiagnosisResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Create a clinical report for a patient assigned to the doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Clinical report created"),
            @ApiResponse(responseCode = "403", description = "Patient not assigned to this doctor")
    })
    @PreAuthorize("hasRole('DOCTOR') and @ownerShipService.isDoctorAssignedToPatient(#patientId)")
    @PostMapping("/{patientId}/clinical-reports")
    public ResponseEntity<?> createClinicalReportForPatient(
            @PathVariable UUID patientId,
            @Valid @RequestBody CreateClinicalReportRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                clinicalReportCommandService.create(request.title(), request.summary(), request.recommendations()),
                ClinicalReportResource::fromDomain,
                HttpStatus.CREATED);
    }

    @Operation(summary = "Create an appointment for a patient assigned to the doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Appointment created"),
            @ApiResponse(responseCode = "403", description = "Patient not assigned to this doctor")
    })
    @PreAuthorize("hasRole('DOCTOR') and @ownerShipService.isDoctorAssignedToPatient(#patientId)")
    @PostMapping("/{patientId}/appointments")
    public ResponseEntity<?> createAppointmentForPatient(
            @PathVariable UUID patientId,
            @Valid @RequestBody CreateAppointmentRequest request) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                appointmentCommandService.create(request.scheduledAt(), request.reason()),
                AppointmentResource::fromDomain,
                HttpStatus.CREATED);
    }
}
