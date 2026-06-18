package com.integravida.IntegraVidaBackend.medical.application.ports.outbound;

import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;

public interface ExternalPatientService {
    boolean existsById(PatientId patientId);
}