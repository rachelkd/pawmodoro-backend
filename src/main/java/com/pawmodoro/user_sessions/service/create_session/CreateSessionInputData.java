package com.pawmodoro.user_sessions.service.create_session;

import com.pawmodoro.user_sessions.entity.SessionType;

/**
 * Input data for creating a new user session.
 * @param sessionType The type of session (focus, short break, or long break)
 * @param durationMinutes The planned duration in minutes
 */
public record CreateSessionInputData(
    SessionType sessionType,
    int durationMinutes) {

}
