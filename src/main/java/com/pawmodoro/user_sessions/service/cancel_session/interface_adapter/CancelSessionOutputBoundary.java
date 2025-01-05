package com.pawmodoro.user_sessions.service.cancel_session.interface_adapter;

import com.pawmodoro.user_sessions.service.cancel_session.CancelSessionOutputData;

/**
 * Output boundary for the cancel session use case.
 * Defines the contract for presenting the output data.
 */
public interface CancelSessionOutputBoundary {
    /**
     * Prepares the response for the cancel session use case.
     * @param output The output data to be presented
     * @return The formatted response DTO
     */
    CancelSessionResponseDto prepareResponse(CancelSessionOutputData output);
}
