package com.pawmodoro.user_sessions.service.cancel_session.interface_adapter;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Builder;

/**
 * Response DTO for the cancel session use case.
 * @param id The session ID
 * @param userId The user ID
 * @param sessionType The type of session (focus, short_break, long_break)
 * @param sessionStartTime When the session started
 * @param sessionEndTime When the session was canceled
 * @param durationMinutes The planned duration in minutes
 * @param completed Whether the session was completed
 * @param interruptionCount Number of interruptions during the session
 */
@Builder
public record CancelSessionResponseDto(
    UUID id,
    UUID userId,
    String sessionType,
    ZonedDateTime sessionStartTime,
    ZonedDateTime sessionEndTime,
    int durationMinutes,
    boolean completed,
    int interruptionCount) {

}
