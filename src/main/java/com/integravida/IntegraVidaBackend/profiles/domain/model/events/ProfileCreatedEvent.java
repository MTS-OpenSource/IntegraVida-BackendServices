package com.integravida.IntegraVidaBackend.profiles.domain.model.events;

import java.util.UUID;

public record ProfileCreatedEvent(UUID profileId, String email, String fullName) {}
