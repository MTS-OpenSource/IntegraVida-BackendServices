package com.integravida.IntegraVidaBackend.monitoring.domain.model.events;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.AlertSeverity;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;

import java.time.LocalDateTime;
import java.util.UUID;

public record GlucoseAlertTriggeredEvent(
        UUID glucoseRecordId,
        PatientId patientId,
        GlucoseValue glucoseValue,
        GlucoseValue minimumRange,
        GlucoseValue maximumRange,
        AlertSeverity severity,
        LocalDateTime measuredAt) {

    public String message() {
        return "Glucose value %s is outside the target range [%s, %s]".formatted(
                glucoseValue.value(),
                minimumRange.value(),
                maximumRange.value());
    }
}
