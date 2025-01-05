package com.pawmodoro.user_sessions.service.complete_session;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.service.complete_session.interface_adapter.CompleteSessionResponseDto;

/**
 * Input boundary for the complete session use case.
 * Defines the contract for completing a session.
 */
public interface CompleteSessionInputBoundary {
    /**
     * Completes a session.
     * @param input The input data containing the session ID
     * @return The response DTO with updated session details
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    CompleteSessionResponseDto execute(CompleteSessionInputData input) throws DatabaseAccessException;
}
