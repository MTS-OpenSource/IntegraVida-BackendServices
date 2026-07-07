package com.integravida.IntegraVidaBackend.patients.application.services;

import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.DoctorRepository;
import com.integravida.IntegraVidaBackend.patients.application.ports.outbound.PatientDoctorRepository;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Doctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.PatientDoctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects.DoctorId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class DoctorQueryService {
    private final DoctorRepository doctorRepository;
    private final PatientDoctorRepository patientDoctorRepository;

    public DoctorQueryService(DoctorRepository doctorRepository,
                              PatientDoctorRepository patientDoctorRepository) {
        this.doctorRepository = doctorRepository;
        this.patientDoctorRepository = patientDoctorRepository;
    }

    public Result<Doctor, ApplicationError> getByProfileId(UUID profileId) {
        return doctorRepository.findByProfileId(profileId)
                .map(Result::<Doctor, ApplicationError>success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("doctor", profileId.toString())));
    }

    public List<PatientDoctor> getPatientsByDoctorId(UUID doctorId) {
        return patientDoctorRepository.findByDoctorId(doctorId);
    }

    public Result<PatientDoctor, ApplicationError> getDoctorByPatientId(UUID patientId) {
        return patientDoctorRepository.findByPatientId(patientId)
                .map(Result::<PatientDoctor, ApplicationError>success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("doctor assignment", patientId.toString())));
    }
}
