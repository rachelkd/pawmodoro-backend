package com.pawmodoro.user_sessions.service.create_session;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.entity.UserSession;

/**
 * Interface for data access operations related to creating user sessions.
 */
public interface CreateSessionDataAccessInterface {
    /**
     * Creates a new user session.
     * @param userSession The session entity to save
     * @return The created session entity
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    UserSession create(UserSession userSession) throws DatabaseAccessException;
}
