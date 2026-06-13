package com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.AlertSeverity;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlucoseRangeTest {

    @Test
    void shouldClassifyValuesOutsideTheRange() {
        var range = GlucoseRange.create(
                UUID.randomUUID(),
                PatientId.of(UUID.randomUUID()),
                GlucoseValue.of(new BigDecimal("70")),
                GlucoseValue.of(new BigDecimal("180")),
                java.time.LocalDateTime.now());

        assertTrue(range.classify(GlucoseValue.of(new BigDecimal("120"))).isEmpty());
        assertEquals(AlertSeverity.LOW, range.classify(GlucoseValue.of(new BigDecimal("65"))).orElseThrow());
        assertEquals(AlertSeverity.CRITICAL, range.classify(GlucoseValue.of(new BigDecimal("20"))).orElseThrow());
    }
}
