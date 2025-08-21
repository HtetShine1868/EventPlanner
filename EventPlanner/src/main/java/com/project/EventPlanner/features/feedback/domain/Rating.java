package com.project.EventPlanner.features.feedback.domain;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Rating {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int value;

    Rating(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value; // Jackson uses this for serialization
    }

    // Accept numbers or enum names for deserialization
    @JsonCreator
    public static Rating fromValue(Object value) {
        if (value instanceof Number) {
            int intValue = ((Number) value).intValue();
            for (Rating r : Rating.values()) {
                if (r.value == intValue) return r;
            }
        } else if (value instanceof String) {
            try {
                return Rating.valueOf(((String) value).toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }
        throw new IllegalArgumentException("Invalid rating value: " + value);
    }
}

