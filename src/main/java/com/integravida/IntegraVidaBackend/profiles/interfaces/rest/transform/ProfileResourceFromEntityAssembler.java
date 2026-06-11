package com.integravida.IntegraVidaBackend.profiles.interfaces.rest.transform;

import com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates.Profile;
import com.integravida.IntegraVidaBackend.profiles.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {

    public static ProfileResource toResourceFromEntity(Profile profile) {
        return new ProfileResource(
                profile.getId(),
                profile.getName().getFirstName(),
                profile.getName().getLastName(),
                profile.getFullName(),
                profile.getEmailAddress(),
                profile.getPhoneNumberStr(),
                profile.getBirthDate()
        );
    }
}
