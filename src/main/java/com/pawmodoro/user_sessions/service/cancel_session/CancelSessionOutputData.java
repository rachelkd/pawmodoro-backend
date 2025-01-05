package com.pawmodoro.user_sessions.service.cancel_session;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.pawmodoro.user_sessions.entity.SessionType;
import lombok.Builder;
import lombok.Getter;

/**
 * Output data for the cancel session use case.
 * Contains all session details after cancellation.
 */
@Getter
@Builder
public class CancelSessionOutputData {
    private final UUID id;
    private final UUID userId;
    private final SessionType sessionType;
    private final ZonedDateTime sessionStartTime;
    private final ZonedDateTime sessionEndTime;
    private final int durationMinutes;
    private final boolean completed;
    private final int interruptionCount;
}
