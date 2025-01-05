package com.pawmodoro.user_sessions.service.update_interruption;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.service.update_interruption.interface_adapter.UpdateInterruptionResponseDto;

/**
 * Input boundary for the update interruption use case.
 * Defines the contract for updating a session's interruption count.
 */
public interface UpdateInterruptionInputBoundary {
    /**
     * Updates the interruption count for a session.
     * @param input The input data containing the session ID
     * @return The response DTO with updated session details
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    UpdateInterruptionResponseDto execute(UpdateInterruptionInputData input) throws DatabaseAccessException;
}
