package com.project.EventPlanner.features.feedback.domain;

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

    public int getNumericValue() {
        return value;
    }
}
