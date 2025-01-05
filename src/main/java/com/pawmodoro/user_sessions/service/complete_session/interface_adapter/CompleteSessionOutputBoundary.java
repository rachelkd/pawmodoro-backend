package com.pawmodoro.user_sessions.service.complete_session.interface_adapter;

import com.pawmodoro.user_sessions.service.complete_session.CompleteSessionOutputData;

/**
 * Output boundary for the complete session use case.
 * Defines the contract for presenting the output data.
 */
public interface CompleteSessionOutputBoundary {
    /**
     * Prepares the response for the complete session use case.
     * @param output The output data to be presented
     * @return The formatted response DTO
     */
    CompleteSessionResponseDto prepareResponse(CompleteSessionOutputData output);
}
