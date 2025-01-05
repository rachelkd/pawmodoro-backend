package com.pawmodoro.user_sessions.service.create_session;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.service.create_session.interface_adapter.CreateSessionResponseDto;

/**
 * Input boundary for creating a new user session.
 */
public interface CreateSessionInputBoundary {
    /**
     * Executes the create session use case.
     * @param input The session details
     * @return The created session response
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    CreateSessionResponseDto execute(CreateSessionInputData input) throws DatabaseAccessException;
}
