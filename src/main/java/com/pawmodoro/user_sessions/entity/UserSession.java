package com.pawmodoro.user_sessions.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Entity representing a user's study session.
 * Maps to the public.user_sessions table in the database.
 */
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

    @NotNull
    private Boolean wasCompleted = false;

    @NotNull
    @Min(0)
    private Integer interruptionCount = 0;

    private ZonedDateTime createdAt;

    // Default constructor
    protected UserSession() {
        // Empty constructor
    }

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

    // Getters

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public ZonedDateTime getSessionStartTime() {
        return sessionStartTime;
    }

    public ZonedDateTime getSessionEndTime() {
        return sessionEndTime;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public Boolean getWasCompleted() {
        return wasCompleted;
    }

    public Integer getInterruptionCount() {
        return interruptionCount;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
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
        this.wasCompleted = true;
        this.sessionEndTime = ZonedDateTime.now();
    }

    /**
     * Cancels the session.
     */
    public void cancel() {
        this.wasCompleted = false;
        this.sessionEndTime = ZonedDateTime.now();
    }
}
