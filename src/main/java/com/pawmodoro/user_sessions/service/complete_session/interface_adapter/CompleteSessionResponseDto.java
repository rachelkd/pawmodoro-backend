package com.pawmodoro.user_sessions.service.complete_session.interface_adapter;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Builder;

/**
 * Response DTO for the complete session use case.
 * @param id Session ID
 * @param userId User ID
 * @param sessionType Type of session (focus, short break, or long break)
 * @param sessionStartTime When the session started
 * @param sessionEndTime When the session ended
 * @param durationMinutes Planned duration in minutes
 * @param completed Whether the session was completed
 * @param interruptionCount Number of interruptions
 */
@Builder
public record CompleteSessionResponseDto(
    UUID id,
    UUID userId,
    String sessionType,
    ZonedDateTime sessionStartTime,
    ZonedDateTime sessionEndTime,
    int durationMinutes,
    boolean completed,
    int interruptionCount) {

}
