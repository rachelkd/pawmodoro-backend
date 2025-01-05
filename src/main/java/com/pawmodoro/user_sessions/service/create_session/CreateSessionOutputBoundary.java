package com.pawmodoro.user_sessions.service.create_session;

import com.pawmodoro.user_sessions.service.create_session.interface_adapter.CreateSessionResponseDto;

/**
 * Output boundary for presenting the results of creating a new study session.
 */
public interface CreateSessionOutputBoundary {
    /**
     * Prepares the response DTO from the output data.
     * @param outputData The output data from the use case
     * @return The formatted response DTO
     */
    CreateSessionResponseDto prepareResponse(CreateSessionOutputData outputData);
}
