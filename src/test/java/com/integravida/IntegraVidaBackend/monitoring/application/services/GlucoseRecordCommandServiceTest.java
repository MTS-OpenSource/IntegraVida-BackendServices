package com.integravida.IntegraVidaBackend.monitoring.application.services;

import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.AlertRepository;
import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.ExternalPatientService;
import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.GlucoseRangeRepository;
import com.integravida.IntegraVidaBackend.monitoring.application.ports.outbound.GlucoseRecordRepository;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.Alert;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.aggregates.GlucoseRange;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.GlucoseValue;
import com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects.PatientId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlucoseRecordCommandServiceTest {

    @Mock
    private GlucoseRecordRepository recordRepository;

    @Mock
    private GlucoseRangeRepository rangeRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private ExternalPatientService externalPatientService;

    @InjectMocks
    private GlucoseRecordCommandService service;

    @Test
    void shouldCreateAlertWhenRecordIsOutsideRange() {
        var patientId = PatientId.of(UUID.randomUUID());
        var range = GlucoseRange.create(
                UUID.randomUUID(),
                patientId,
                GlucoseValue.of(new BigDecimal("70")),
                GlucoseValue.of(new BigDecimal("180")),
                LocalDateTime.now());

        when(externalPatientService.existsById(patientId)).thenReturn(true);
        when(rangeRepository.findActiveByPatientId(patientId)).thenReturn(Optional.of(range));
        when(recordRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.create(patientId, GlucoseValue.of(new BigDecimal("250")), LocalDateTime.now());

        assertTrue(result.isSuccess());
        verify(alertRepository).save(any(Alert.class));
    }
}
