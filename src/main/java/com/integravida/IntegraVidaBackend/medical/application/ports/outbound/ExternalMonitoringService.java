package com.integravida.IntegraVidaBackend.medical.application.ports.outbound;

import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;

public interface ExternalMonitoringService {
    boolean hasGlucoseRecordsByPatientId(PatientId patientId);
}