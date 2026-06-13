package com.integravida.IntegraVidaBackend.monitoring.domain.model.valueobjects;

import java.math.BigDecimal;

public enum AlertSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public static AlertSeverity fromDeviationRatio(BigDecimal ratio) {
        if (ratio.compareTo(new BigDecimal("0.10")) <= 0) {
            return LOW;
        }
        if (ratio.compareTo(new BigDecimal("0.25")) <= 0) {
            return MEDIUM;
        }
        if (ratio.compareTo(new BigDecimal("0.50")) <= 0) {
            return HIGH;
        }
        return CRITICAL;
    }
}
