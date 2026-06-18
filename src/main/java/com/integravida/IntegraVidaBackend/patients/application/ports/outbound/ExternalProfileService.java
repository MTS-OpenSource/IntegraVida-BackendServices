package com.integravida.IntegraVidaBackend.patients.application.ports.outbound;

import java.util.Optional;
import java.util.UUID;

public interface ExternalProfileService {
    boolean existsById(UUID profileId);

    Optional<String> getFullNameByProfileId(UUID profileId);

    Optional<String> getEmailByProfileId(UUID profileId);
}
