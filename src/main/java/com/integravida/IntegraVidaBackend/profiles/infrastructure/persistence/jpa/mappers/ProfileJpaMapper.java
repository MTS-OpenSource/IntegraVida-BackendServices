package com.integravida.IntegraVidaBackend.profiles.infrastructure.persistence.jpa.mappers;

import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.DateOfBirth;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.EmailAddress;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.PersonName;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.PhoneNumber;
import com.integravida.IntegraVidaBackend.profiles.infrastructure.persistence.jpa.entities.ProfileEntity;

public final class ProfileJpaMapper {
    private ProfileJpaMapper() {}

    public static Profile toDomain(ProfileEntity e) {
        return Profile.reconstitute(
                e.getId(),
                PersonName.of(e.getFirstName(), e.getLastName()),
                EmailAddress.of(e.getEmail()),
                PhoneNumber.of(e.getPhoneNumber()),
                DateOfBirth.of(e.getDateOfBirth()),
                e.getCreatedAt(),
                e.getUpdatedAt());
    }

    public static ProfileEntity toEntity(Profile p) {
        ProfileEntity e = new ProfileEntity();
        e.setId(p.getId());
        e.setFirstName(p.getName().firstName());
        e.setLastName(p.getName().lastName());
        e.setEmail(p.getEmail().value());
        e.setPhoneNumber(p.getPhoneNumber().value());
        e.setDateOfBirth(p.getDateOfBirth().value());
        e.setCreatedAt(p.getCreatedAt());
        e.setUpdatedAt(p.getUpdatedAt());
        return e;
    }
}
