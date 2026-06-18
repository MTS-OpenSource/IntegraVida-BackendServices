package com.integravida.IntegraVidaBackend.medical.infrastructure.integration;
import com.integravida.IntegraVidaBackend.medical.application.ports.outbound.ExternalPatientService;
import com.integravida.IntegraVidaBackend.medical.domain.model.valueobjects.PatientId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
@Service("medicalRestExternalPatientServiceAdapter")
public class RestExternalPatientServiceAdapter implements ExternalPatientService {
    private final RestClient restClient;
    private final boolean localFallback;

    public RestExternalPatientServiceAdapter(@Value("${medical.external-patient.base-url:}") String baseUrl) {
        this.localFallback = baseUrl == null || baseUrl.isBlank();
        this.restClient = this.localFallback ? null : RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public boolean existsById(PatientId patientId) {
        if (localFallback) {
            return true;
        }

        try {
            restClient.get()
                    .uri("/api/v1/patients/{id}", patientId.value())
                    .retrieve()
                    .toBodilessEntity();

            return true;
        } catch (RestClientResponseException exception) {
            return exception.getStatusCode().value() != 404;
        } catch (RestClientException exception) {
            return false;
        }
    }
}