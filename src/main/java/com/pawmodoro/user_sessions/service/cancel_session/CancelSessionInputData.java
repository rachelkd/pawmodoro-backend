package com.pawmodoro.user_sessions.service.cancel_session;

import java.util.UUID;

/**
 * Input data for canceling a session.
 * Contains only the session ID since it's the only required information.
 * @param sessionId The ID of the session to cancel
 */
public record CancelSessionInputData(UUID sessionId) {

}
