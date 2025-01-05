package com.pawmodoro.user_sessions.service.create_session;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.pawmodoro.user_sessions.entity.SessionType;

/**
 * Data Transfer Object for session data from the database.
 */
public final class CreateSessionDataAccessDto {
    private final UUID id;
    private final UUID userId;
    private final SessionType sessionType;
    private final ZonedDateTime sessionStartTime;
    private final ZonedDateTime sessionEndTime;
    private final int durationMinutes;
    private final boolean wasCompleted;
    private final int interruptionCount;

    private CreateSessionDataAccessDto(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.sessionType = builder.sessionType;
        this.sessionStartTime = builder.sessionStartTime;
        this.sessionEndTime = builder.sessionEndTime;
        this.durationMinutes = builder.durationMinutes;
        this.wasCompleted = builder.wasCompleted;
        this.interruptionCount = builder.interruptionCount;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public ZonedDateTime getSessionStartTime() {
        return sessionStartTime;
    }

    public ZonedDateTime getSessionEndTime() {
        return sessionEndTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public boolean isWasCompleted() {
        return wasCompleted;
    }

    public int getInterruptionCount() {
        return interruptionCount;
    }

    /**
     * Creates a new builder instance.
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for CreateSessionDataAccessDto.
     */
    public static final class Builder {
        private UUID id;
        private UUID userId;
        private SessionType sessionType;
        private ZonedDateTime sessionStartTime;
        private ZonedDateTime sessionEndTime;
        private int durationMinutes;
        private boolean wasCompleted;
        private int interruptionCount;

        private Builder() {
            // Prevent instantiation
        }

        /**
         * Sets the session ID.
         * @param val The session ID
         * @return This builder
         */
        public Builder id(UUID val) {
            this.id = val;
            return this;
        }

        /**
         * Sets the user ID.
         * @param val The user ID
         * @return This builder
         */
        public Builder userId(UUID val) {
            this.userId = val;
            return this;
        }

        /**
         * Sets the session type.
         * @param val The session type
         * @return This builder
         */
        public Builder sessionType(SessionType val) {
            this.sessionType = val;
            return this;
        }

        /**
         * Sets the session start time.
         * @param val The session start time
         * @return This builder
         */
        public Builder sessionStartTime(ZonedDateTime val) {
            this.sessionStartTime = val;
            return this;
        }

        /**
         * Sets the session end time.
         * @param val The session end time
         * @return This builder
         */
        public Builder sessionEndTime(ZonedDateTime val) {
            this.sessionEndTime = val;
            return this;
        }

        /**
         * Sets the duration in minutes.
         * @param val The duration in minutes
         * @return This builder
         */
        public Builder durationMinutes(int val) {
            this.durationMinutes = val;
            return this;
        }

        /**
         * Sets whether the session was completed.
         * @param val Whether the session was completed
         * @return This builder
         */
        public Builder wasCompleted(boolean val) {
            this.wasCompleted = val;
            return this;
        }

        /**
         * Sets the interruption count.
         * @param val The number of interruptions
         * @return This builder
         */
        public Builder interruptionCount(int val) {
            this.interruptionCount = val;
            return this;
        }

        /**
         * Builds the data access DTO.
         * @return The built data access DTO
         */
        public CreateSessionDataAccessDto build() {
            return new CreateSessionDataAccessDto(this);
        }
    }
}
