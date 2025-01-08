package com.pawmodoro.user_sessions.service.cancel_session;

import java.util.UUID;

import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.entity.UserSession;

/**
 * Data access interface for canceling sessions.
 */
public interface CancelSessionDataAccessInterface {
    /**
     * Gets a session by ID.
     * @param sessionId The ID of the session to get
     * @param token Optional authorization token for beacon requests
     * @return The session
     * @throws DatabaseAccessException if there's an error accessing the database
     * @throws AuthenticationException if the token is invalid or user is not authorized
     */
    UserSession getSession(UUID sessionId, String token) throws DatabaseAccessException, AuthenticationException;

    /**
     * Updates the cancellation status and end time for a session.
     * @param session The session to update
     * @param token Optional authorization token for beacon requests
     * @return The updated session
     * @throws DatabaseAccessException if there's an error accessing the database
     * @throws AuthenticationException if the token is invalid or user is not authorized
     */
    UserSession updateCancellation(UserSession session,
        String token) throws DatabaseAccessException, AuthenticationException;
}
