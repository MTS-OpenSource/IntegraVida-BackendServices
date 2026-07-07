package com.integravida.IntegraVidaBackend.iam.domain.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Roles, String> {

    @Override
    public String convertToDatabaseColumn(Roles role) {
        if (role == null) return null;
        return role.name().charAt(0) + role.name().substring(1).toLowerCase();
    }

    @Override
    public Roles convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return Roles.valueOf(dbData.toUpperCase());
    }
}
