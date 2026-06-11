package com.integravida.IntegraVidaBackend.profiles.domain.model.events;

/**
 * Domain event published when a new Profile is created.
 * Consumed by the patients and doctors bounded contexts
 * to automatically create their respective records.
 */
public record ProfileCreatedEvent(Long profileId, String email, String fullName) {}
