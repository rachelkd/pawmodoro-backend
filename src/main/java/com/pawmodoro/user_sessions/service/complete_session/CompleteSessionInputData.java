package com.pawmodoro.user_sessions.service.complete_session;

import java.util.UUID;

/**
 * Input data for completing a session.
 * Contains only the session ID since it's the only required information.
 * @param sessionId The ID of the session to complete
 */
public record CompleteSessionInputData(
    UUID sessionId) {

}
