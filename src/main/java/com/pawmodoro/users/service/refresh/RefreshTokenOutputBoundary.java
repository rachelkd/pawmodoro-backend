package com.pawmodoro.users.service.refresh;

import com.pawmodoro.users.service.refresh.interface_adapter.RefreshTokenResponseDto;

/**
 * Output boundary for formatting refresh token responses.
 * This interface defines how the use case output should be formatted into responses.
 */
public interface RefreshTokenOutputBoundary {
    /**
     * Formats a refresh token response.
     * @param outputData the output data from the refresh token use case
     * @return formatted refresh token response DTO
     */
    RefreshTokenResponseDto prepareResponse(RefreshTokenOutputData outputData);
}
