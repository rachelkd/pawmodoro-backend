package com.pawmodoro.user_sessions.service.create_session.interface_adapter;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Builder;

/**
 * Data Transfer Object for session creation API response.
 * @param id The session ID
 * @param userId The user ID
 * @param sessionType The type of session (focus, short break, or long break)
 * @param sessionStartTime When the session started
 * @param sessionEndTime When the session is scheduled to end
 * @param durationMinutes The planned duration in minutes
 * @param completed Whether the session was completed
 * @param interruptionCount The number of times the session was interrupted
 */
@Builder
public record CreateSessionResponseDto(
    UUID id,
    UUID userId,
    String sessionType,
    ZonedDateTime sessionStartTime,
    ZonedDateTime sessionEndTime,
    int durationMinutes,
    boolean completed,
    int interruptionCount) {

}
