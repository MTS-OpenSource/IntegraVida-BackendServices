package com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;

public interface ExternalPatientService {
    boolean existsById(PatientId patientId);
}
