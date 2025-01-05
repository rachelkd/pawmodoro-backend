package com.pawmodoro.user_sessions.service.create_session;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.pawmodoro.user_sessions.entity.SessionType;

import lombok.Builder;
import lombok.Getter;

/**
 * Data Transfer Object for session data from the database.
 */
@Getter
@Builder
public class CreateSessionDataAccessDto {
    private final UUID id;
    private final UUID userId;
    private final SessionType sessionType;
    private final ZonedDateTime sessionStartTime;
    private final ZonedDateTime sessionEndTime;
    private final int durationMinutes;
    private final boolean wasCompleted;
    private final int interruptionCount;
}
