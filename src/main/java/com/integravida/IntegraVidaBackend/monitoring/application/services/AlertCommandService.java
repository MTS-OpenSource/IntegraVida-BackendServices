package com.integravida.IntegraVidaBackend.monitoring.application.services;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.AlertRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.Alert;
import com.integravida.IntegraVidaBackend.shared.application.result.ApplicationError;
import com.integravida.IntegraVidaBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AlertCommandService {
    private final AlertRepository repository;

    public AlertCommandService(AlertRepository repository) {
        this.repository = repository;
    }

    public Result<Alert, ApplicationError> markAsRead(UUID alertId) {
        return repository.findById(alertId)
                .map(alert -> {
                    alert.markAsRead(LocalDateTime.now());
                    return Result.<Alert, ApplicationError>success(repository.save(alert));
                })
                .orElseGet(() -> Result.<Alert, ApplicationError>failure(ApplicationError.notFound("alert", alertId.toString())));
    }
}
