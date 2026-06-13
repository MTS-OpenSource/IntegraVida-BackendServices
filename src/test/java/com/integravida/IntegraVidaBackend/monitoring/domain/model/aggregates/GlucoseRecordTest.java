package com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.events.GlucoseAlertTriggeredEvent;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GlucoseRecordTest {

    @Test
    void shouldRegisterAlertEventWhenValueIsOutsideRange() {
        var patientId = PatientId.of(UUID.randomUUID());
        var range = GlucoseRange.create(
                UUID.randomUUID(),
                patientId,
                GlucoseValue.of(new BigDecimal("70")),
                GlucoseValue.of(new BigDecimal("180")),
                java.time.LocalDateTime.now());

        var record = GlucoseRecord.create(
                UUID.randomUUID(),
                patientId,
                GlucoseValue.of(new BigDecimal("250")),
                range,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now());

        assertTrue(record.getTriggeredSeverity().isPresent());
        assertTrue(record.domainEvents().stream().anyMatch(GlucoseAlertTriggeredEvent.class::isInstance));
    }
}
