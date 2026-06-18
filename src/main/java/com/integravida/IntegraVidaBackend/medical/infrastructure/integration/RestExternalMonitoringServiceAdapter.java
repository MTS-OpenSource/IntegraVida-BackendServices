package com.integravida.IntegraVidaBackend.medical.infrastructure.integration;

import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.ExternalMonitoringService;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class RestExternalMonitoringServiceAdapter implements ExternalMonitoringService {
    private final RestClient restClient;
    private final boolean localFallback;

    public RestExternalMonitoringServiceAdapter(@Value("${medical.external-monitoring.base-url:}") String baseUrl) {
        this.localFallback = baseUrl == null || baseUrl.isBlank();
        this.restClient = this.localFallback ? null : RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public boolean hasGlucoseRecordsByPatientId(PatientId patientId) {
        if (localFallback) {
            return true;
        }

        try {
            var records = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/glucose-records")
                            .queryParam("patientId", patientId.value())
                            .build())
                    .retrieve()
                    .body(List.class);

            return records != null && !records.isEmpty();
        } catch (RestClientResponseException exception) {
            return false;
        } catch (RestClientException exception) {
            return false;
        }
    }
}