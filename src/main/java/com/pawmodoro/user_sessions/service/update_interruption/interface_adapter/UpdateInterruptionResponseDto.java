package com.pawmodoro.user_sessions.service.update_interruption.interface_adapter;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Builder;

/**
 * Response DTO for the update interruption use case.
 * Contains all session details after the interruption is recorded.
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
public record UpdateInterruptionResponseDto(
    UUID id,
    UUID userId,
    String sessionType,
    ZonedDateTime sessionStartTime,
    ZonedDateTime sessionEndTime,
    int durationMinutes,
    boolean completed,
    int interruptionCount) {

}
