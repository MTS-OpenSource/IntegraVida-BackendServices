package com.integravida.IntegraVidaBackend.monitoring.application.services;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.AlertRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.Alert;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlertQueryService {
    private final AlertRepository repository;

    public AlertQueryService(AlertRepository repository) {
        this.repository = repository;
    }

    public Result<Alert, ApplicationError> getById(UUID alertId) {
        return repository.findById(alertId)
                .map(alert -> Result.<Alert, ApplicationError>success(alert))
                .orElseGet(() -> Result.<Alert, ApplicationError>failure(ApplicationError.notFound("alert", alertId.toString())));
    }

    public List<Alert> findByPatientId(PatientId patientId) {
        return repository.findByPatientId(patientId);
    }

    public List<Alert> findUnreadByPatientId(PatientId patientId) {
        return repository.findUnreadByPatientId(patientId);
    }
}
