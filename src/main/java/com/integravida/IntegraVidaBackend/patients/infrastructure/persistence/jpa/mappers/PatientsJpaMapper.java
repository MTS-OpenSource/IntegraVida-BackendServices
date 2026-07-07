package com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.mappers;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Doctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Medication;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.MedicationIntake;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.PatientDoctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Treatment;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.MedicationSchedule;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.TreatmentStatus;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.DoctorEntity;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.MedicationEntity;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.MedicationIntakeEntity;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.PatientDoctorEntity;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.PatientEntity;
import com.integravida.IntegraVidaBackend.patients.infrastructure.persistence.jpa.entities.TreatmentEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class PatientsJpaMapper {
    private PatientsJpaMapper() {
    }

    public static Patient toDomain(PatientEntity entity) {
        return Patient.reconstitute(
                PatientId.of(entity.getId()),
                entity.getProfileId(),
                entity.getMedicalRecordNumber(),
                entity.getNotes(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static PatientEntity toEntity(Patient domain) {
        var entity = new PatientEntity();
        entity.setId(domain.getId().value());
        entity.setProfileId(domain.getProfileId());
        entity.setMedicalRecordNumber(domain.getMedicalRecordNumber());
        entity.setNotes(domain.getNotes());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public static Treatment toDomain(TreatmentEntity entity) {
        return Treatment.reconstitute(
                entity.getId(),
                PatientId.of(entity.getPatientId()),
                entity.getName(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                TreatmentStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static TreatmentEntity toEntity(Treatment domain) {
        var entity = new TreatmentEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId().value());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setStatus(domain.getStatus().name());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public static Medication toDomain(MedicationEntity entity) {
        return Medication.reconstitute(
                entity.getId(),
                PatientId.of(entity.getPatientId()),
                entity.getTreatmentId(),
                entity.getName(),
                entity.getDosage(),
                MedicationSchedule.of(
                        decodeDays(entity.getDaysOfWeek()),
                        decodeTimes(entity.getDoseTimes()),
                        entity.getInstructions()),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDiscontinuedAt());
    }

    public static MedicationEntity toEntity(Medication domain) {
        var entity = new MedicationEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId().value());
        entity.setTreatmentId(domain.getTreatmentId());
        entity.setName(domain.getName());
        entity.setDosage(domain.getDosage());
        entity.setDaysOfWeek(encodeDays(domain.getSchedule().daysOfWeek()));
        entity.setDoseTimes(encodeTimes(domain.getSchedule().doseTimes()));
        entity.setInstructions(domain.getSchedule().instructions());
        entity.setActive(domain.isActive());
        entity.setDiscontinuedAt(domain.getDiscontinuedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public static MedicationIntake toDomain(MedicationIntakeEntity entity) {
        return MedicationIntake.reconstitute(
                entity.getId(),
                entity.getMedicationId(),
                PatientId.of(entity.getPatientId()),
                entity.getTakenAt(),
                entity.getNotes(),
                entity.getCreatedAt());
    }

    public static MedicationIntakeEntity toEntity(MedicationIntake domain) {
        var entity = new MedicationIntakeEntity();
        entity.setId(domain.getId());
        entity.setMedicationId(domain.getMedicationId());
        entity.setPatientId(domain.getPatientId().value());
        entity.setTakenAt(domain.getTakenAt());
        entity.setNotes(domain.getNotes());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public static Doctor toDomain(DoctorEntity entity) {
        return Doctor.reconstitute(
                DoctorId.of(entity.getId()),
                entity.getProfileId(),
                entity.getDoctorRecordNumber(),
                entity.getNotes(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static DoctorEntity toEntity(Doctor domain) {
        var entity = new DoctorEntity();
        entity.setId(domain.getId().value());
        entity.setProfileId(domain.getProfileId());
        entity.setDoctorRecordNumber(domain.getDoctorRecordNumber());
        entity.setNotes(domain.getNotes());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public static PatientDoctor toDomain(PatientDoctorEntity entity) {
        return new PatientDoctor(
                entity.getId(),
                entity.getPatientId(),
                entity.getDoctorId(),
                entity.getAssignedAt());
    }

    public static PatientDoctorEntity toEntity(PatientDoctor domain) {
        var entity = new PatientDoctorEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId());
        entity.setDoctorId(domain.getDoctorId());
        entity.setAssignedAt(domain.getAssignedAt());
        return entity;
    }

    private static String encodeDays(List<DayOfWeek> daysOfWeek) {
        return daysOfWeek.stream().map(DayOfWeek::name).collect(Collectors.joining(","));
    }

    private static String encodeTimes(List<LocalTime> doseTimes) {
        return doseTimes.stream().map(LocalTime::toString).collect(Collectors.joining(","));
    }

    private static List<DayOfWeek> decodeDays(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
                .filter(token -> !token.isBlank())
                .map(String::trim)
                .map(DayOfWeek::valueOf)
                .toList();
    }

    private static List<LocalTime> decodeTimes(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
                .filter(token -> !token.isBlank())
                .map(String::trim)
                .map(LocalTime::parse)
                .toList();
    }
}
