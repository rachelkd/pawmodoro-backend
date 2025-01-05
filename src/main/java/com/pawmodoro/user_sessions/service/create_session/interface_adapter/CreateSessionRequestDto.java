package com.pawmodoro.user_sessions.service.create_session.interface_adapter;

import com.pawmodoro.user_sessions.entity.SessionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating a new study session request.
 * @param sessionType The type of session (focus, short break, or long break)
 * @param durationMinutes The planned duration in minutes
 */
public record CreateSessionRequestDto(
    @NotNull(message = "Session type is required") SessionType sessionType,
    @NotNull(message = "Duration is required") @Min(value = 1,
        message = "Duration must be at least 1 minute") Integer durationMinutes) {

}
