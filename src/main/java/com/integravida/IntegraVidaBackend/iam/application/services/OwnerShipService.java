package com.integravida.IntegraVidaBackend.iam.application.services;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.JwtClaimsExtractor;
import com.integravida.IntegraVidaBackend.patients.application.services.DoctorQueryService;
import com.integravida.IntegraVidaBackend.patients.application.services.PatientQueryService;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Doctor;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.Patient;
import com.integravida.IntegraVidaBackend.patients.domain.model.aggregates.PatientDoctor;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OwnerShipService {
    private final PatientQueryService patientQueryService;
    private final DoctorQueryService doctorQueryService;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public OwnerShipService(PatientQueryService patientQueryService, DoctorQueryService doctorQueryService, JwtClaimsExtractor jwtClaimsExtractor) {
        this.patientQueryService = patientQueryService;
        this.doctorQueryService = doctorQueryService;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    public boolean isOwnerPatient(UUID patientId){
        String jwtPatientId = jwtClaimsExtractor.extractPatientId();
        if(jwtPatientId == null) return false;
        return patientId.toString().equals(jwtPatientId);
    }
    public boolean isDoctorAssignedToPatient(UUID patientId){
        String jwtDoctorId = jwtClaimsExtractor.extractDoctorId();
        if(jwtDoctorId == null) return false;

        UUID doctorId = UUID.fromString(jwtDoctorId);
        var result = doctorQueryService.getDoctorByPatientId(patientId);

        if (result instanceof Result.Success<PatientDoctor, ?> success) {
            return success.toOptional().get().getDoctorId().equals(doctorId);
        }
        return false;
    }
    public boolean isOwnerDoctor(UUID doctorId){
        String jwtDoctorId = jwtClaimsExtractor.extractDoctorId();
        if(jwtDoctorId == null ) return false;
        return doctorId.toString().equals(jwtDoctorId);
    }


}
