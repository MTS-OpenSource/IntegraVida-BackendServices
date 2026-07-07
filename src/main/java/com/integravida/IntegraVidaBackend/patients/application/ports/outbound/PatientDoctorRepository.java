package com.integravida.IntegraVidaBackend.patients.application.ports.outbound;

import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.PatientDoctor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientDoctorRepository {
    PatientDoctor save(PatientDoctor patientDoctor);

    List<PatientDoctor> findByDoctorId(UUID doctorId);

    Optional<PatientDoctor> findByPatientId(UUID patientId);

    boolean existsByPatientIdAndDoctorId(UUID patientId, UUID doctorId);
}
