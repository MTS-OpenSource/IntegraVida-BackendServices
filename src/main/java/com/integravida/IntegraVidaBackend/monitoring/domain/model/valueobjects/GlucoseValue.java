package com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects;

import java.math.BigDecimal;
import java.util.Objects;

public record GlucoseValue(BigDecimal value) {

    public GlucoseValue {
        Objects.requireNonNull(value, "glucose value is required");
        if (value.signum() < 0) {
            throw new IllegalArgumentException("glucose value must be positive");
        }
    }

    public static GlucoseValue of(BigDecimal value) {
        return new GlucoseValue(value);
    }

    public boolean isBelow(BigDecimal threshold) {
        return value.compareTo(threshold) < 0;
    }

    public boolean isAbove(BigDecimal threshold) {
        return value.compareTo(threshold) > 0;
    }
}
