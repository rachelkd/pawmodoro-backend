package com.pawmodoro.user_sessions.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity representing a user's study session.
 * Maps to the public.user_sessions table in the database.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSession {
    private UUID id;

    @NotNull
    private UUID userId;

    @NotNull
    private ZonedDateTime sessionStartTime;

    @NotNull
    private ZonedDateTime sessionEndTime;

    @NotNull
    private SessionType sessionType;

    @NotNull
    @Min(1)
    private Integer durationMinutes;

    @JsonProperty("completed")
    @JsonAlias("was_completed")
    private boolean completed = false;

    @NotNull
    @Min(0)
    private Integer interruptionCount = 0;

    private ZonedDateTime createdAt;

    /**
     * Creates a new UserSession instance.
     * The user ID and ID will be set by Supabase.
     * @param sessionType The type of session (focus, short break, or long break)
     * @param durationMinutes The planned duration in minutes
     */
    public UserSession(SessionType sessionType, int durationMinutes) {
        this.sessionType = sessionType;
        this.durationMinutes = durationMinutes;
        this.sessionStartTime = ZonedDateTime.now();
        this.sessionEndTime = this.sessionStartTime;
    }

    // Business methods

    /**
     * Records an interruption in the session.
     */
    public void recordInterruption() {
        this.interruptionCount++;
    }

    /**
     * Completes the session successfully.
     */
    public void complete() {
        this.completed = true;
        this.sessionEndTime = ZonedDateTime.now();
    }

    /**
     * Cancels the session, setting the end time to now and completed to false.
     */
    public void cancel() {
        this.completed = false;
        this.sessionEndTime = ZonedDateTime.now();
    }
}
