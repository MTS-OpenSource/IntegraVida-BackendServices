package com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.profiles.domain.model.events.ProfileCreatedEvent;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.DateOfBirth;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.EmailAddress;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.PersonName;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.PhoneNumber;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Profile extends AbstractDomainAggregateRoot<Profile> {
    private final UUID id;
    private PersonName name;
    private final EmailAddress email;
    private PhoneNumber phoneNumber;
    private DateOfBirth dateOfBirth;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Profile(UUID id, PersonName name, EmailAddress email,
                    PhoneNumber phoneNumber, DateOfBirth dateOfBirth,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id          = Objects.requireNonNull(id, "id is required");
        this.name        = Objects.requireNonNull(name, "name is required");
        this.email       = Objects.requireNonNull(email, "email is required");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "phoneNumber is required");
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth, "dateOfBirth is required");
        this.createdAt   = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt   = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public static Profile create(UUID id, PersonName name, EmailAddress email,
                                 PhoneNumber phoneNumber, DateOfBirth dateOfBirth,
                                 LocalDateTime createdAt) {
        var profile = new Profile(id, name, email, phoneNumber, dateOfBirth, createdAt, createdAt);
        profile.registerDomainEvent(new ProfileCreatedEvent(id, email.value(), name.fullName()));
        return profile;
    }

    public static Profile reconstitute(UUID id, PersonName name, EmailAddress email,
                                       PhoneNumber phoneNumber, DateOfBirth dateOfBirth,
                                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Profile(id, name, email, phoneNumber, dateOfBirth, createdAt, updatedAt);
    }

    public void update(PersonName name, PhoneNumber phoneNumber,
                       DateOfBirth dateOfBirth, LocalDateTime updatedAt) {
        this.name        = Objects.requireNonNull(name, "name is required");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "phoneNumber is required");
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth, "dateOfBirth is required");
        this.updatedAt   = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public UUID           getId()          { return id; }
    public PersonName     getName()        { return name; }
    public EmailAddress   getEmail()       { return email; }
    public PhoneNumber    getPhoneNumber() { return phoneNumber; }
    public DateOfBirth    getDateOfBirth() { return dateOfBirth; }
    public LocalDateTime  getCreatedAt()   { return createdAt; }
    public LocalDateTime  getUpdatedAt()   { return updatedAt; }
}
