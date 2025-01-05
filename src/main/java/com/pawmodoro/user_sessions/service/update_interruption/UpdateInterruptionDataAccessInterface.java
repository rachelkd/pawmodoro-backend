package com.pawmodoro.user_sessions.service.update_interruption;

import java.util.UUID;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.entity.UserSession;

/**
 * Data access interface for updating session interruptions.
 */
public interface UpdateInterruptionDataAccessInterface {
    /**
     * Gets a session by its ID.
     * @param sessionId The ID of the session to get
     * @return The session
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    UserSession getSession(UUID sessionId) throws DatabaseAccessException;

    /**
     * Updates a session in the database.
     * @param session The session to update
     * @return The updated session
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    UserSession updateSession(UserSession session) throws DatabaseAccessException;
}
