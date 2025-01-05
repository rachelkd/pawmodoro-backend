package com.pawmodoro.user_sessions.service.complete_session;

import java.util.UUID;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.entity.UserSession;

/**
 * Data access interface for completing sessions.
 */
public interface CompleteSessionDataAccessInterface {
    /**
     * Gets a session by ID.
     * @param sessionId The ID of the session to get
     * @return The session
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    UserSession getSession(UUID sessionId) throws DatabaseAccessException;

    /**
     * Updates the completion status and end time for a session.
     * @param session The session to update
     * @return The updated session
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    UserSession updateCompletion(UserSession session) throws DatabaseAccessException;
}
