package com.integravida.IntegraVidaBackend.patients.application.ports.outbound;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Doctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.DoctorId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository {
    Doctor save(Doctor doctor);

    List<Doctor> findAll();

    Optional<Doctor> findById(DoctorId id);

    Optional<Doctor> findByProfileId(UUID profileId);

    boolean existsByProfileId(UUID profileId);

    boolean existsByDoctorRecordNumber(String doctorRecordNumber);
}
