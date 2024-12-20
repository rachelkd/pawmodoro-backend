package com.pawmodoro.users.service.login;

import com.pawmodoro.users.service.login.interface_adapter.LoginResponseDto;

/**
 * Output boundary for formatting login responses.
 * This interface defines how the use case output should be formatted into responses.
 * Consistently uses LoginOutputData for both success and error cases to maintain
 * clean separation between use case and presentation layers.
 */
public interface LoginOutputBoundary {
    /**
     * Formats a login response, whether successful or not.
     * @param outputData the output data from the login use case
     * @return formatted login response DTO
     */
    LoginResponseDto prepareResponse(LoginOutputData outputData);
}
