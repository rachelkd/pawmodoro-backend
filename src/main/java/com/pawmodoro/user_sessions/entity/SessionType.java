package com.pawmodoro.user_sessions.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the different types of sessions available in the Pawmodoro application.
 */
public enum SessionType {
    FOCUS("focus"), SHORT_BREAK("short_break"), LONG_BREAK("long_break");

    private final String value;

    SessionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Creates a SessionType from its string value.
     * Used by Jackson for JSON deserialization.
     * Case-insensitive comparison is used to handle both uppercase and lowercase values.
     * @param value The string value to convert
     * @return The corresponding SessionType
     * @throws IllegalArgumentException if the value doesn't match any SessionType
     */
    @JsonCreator
    public static SessionType fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Session type cannot be null");
        }

        final String normalizedValue = value.toLowerCase();
        for (SessionType type : SessionType.values()) {
            if (type.value.equals(normalizedValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid session type: " + value);
    }
}
