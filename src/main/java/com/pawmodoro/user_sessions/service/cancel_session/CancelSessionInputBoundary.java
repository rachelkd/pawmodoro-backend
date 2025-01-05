package com.pawmodoro.user_sessions.service.cancel_session;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.service.cancel_session.interface_adapter.CancelSessionResponseDto;

/**
 * Input boundary for the cancel session use case.
 * Defines the contract for canceling a session.
 */
public interface CancelSessionInputBoundary {
    /**
     * Cancels a session.
     * @param input The input data containing the session ID
     * @return The response DTO with updated session details
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    CancelSessionResponseDto execute(CancelSessionInputData input) throws DatabaseAccessException;
}
