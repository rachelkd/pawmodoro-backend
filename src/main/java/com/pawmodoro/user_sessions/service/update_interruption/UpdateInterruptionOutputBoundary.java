package com.pawmodoro.user_sessions.service.update_interruption;

import com.pawmodoro.user_sessions.service.update_interruption.interface_adapter.UpdateInterruptionResponseDto;

/**
 * Output boundary for the update interruption use case.
 * Defines the contract for preparing the response after updating a session's interruption count.
 */
public interface UpdateInterruptionOutputBoundary {
    /**
     * Prepares the response after updating a session's interruption count.
     * @param outputData The output data containing the updated session details
     * @return The response DTO with formatted session details
     */
    UpdateInterruptionResponseDto prepareResponse(UpdateInterruptionOutputData outputData);
}
