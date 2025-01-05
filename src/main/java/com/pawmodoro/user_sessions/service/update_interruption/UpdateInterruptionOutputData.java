package com.pawmodoro.user_sessions.service.update_interruption;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.pawmodoro.user_sessions.entity.SessionType;
import lombok.Builder;
import lombok.Getter;

/**
 * Output data for the update interruption use case.
 * Contains all session details after the interruption is recorded.
 */
@Getter
@Builder
public class UpdateInterruptionOutputData {
    private final UUID id;
    private final UUID userId;
    private final SessionType sessionType;
    private final ZonedDateTime sessionStartTime;
    private final ZonedDateTime sessionEndTime;
    private final int durationMinutes;
    private final boolean completed;
    private final int interruptionCount;
}
