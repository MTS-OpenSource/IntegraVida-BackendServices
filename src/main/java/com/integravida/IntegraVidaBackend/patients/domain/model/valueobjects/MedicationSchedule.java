package com.integravida.IntegraVidaBackend.patients.domain.model.valueobjects;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public record MedicationSchedule(List<DayOfWeek> daysOfWeek,
                                 List<LocalTime> doseTimes,
                                 String instructions) {
    public MedicationSchedule {
        daysOfWeek = List.copyOf(requireList(daysOfWeek, "daysOfWeek"));
        doseTimes = List.copyOf(requireList(doseTimes, "doseTimes"));
        instructions = Objects.requireNonNull(instructions, "instructions is required");

        if (instructions.isBlank()) {
            throw new IllegalArgumentException("instructions is required");
        }
    }

    public static MedicationSchedule of(List<DayOfWeek> daysOfWeek,
                                        List<LocalTime> doseTimes,
                                        String instructions) {
        return new MedicationSchedule(daysOfWeek, doseTimes, instructions);
    }

    private static <T> List<T> requireList(List<T> values, String name) {
        var copy = Objects.requireNonNull(values, name + " is required");
        if (copy.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be empty");
        }
        if (copy.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(name + " cannot contain null values");
        }
        return copy;
    }
}
