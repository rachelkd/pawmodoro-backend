package com.pawmodoro.users.login;

/**
 * Presenter interface for formatting login responses.
 */
public interface LoginOutputBoundary {
    /**
     * Formats a successful login response.
     * @param outputData the output data from the login use case
     * @return formatted login response DTO
     */
    LoginResponseDTO formatSuccessResponse(LoginOutputData outputData);

    /**
     * Formats a failed login response.
     * @param error the error message describing why login failed
     * @return formatted login response DTO with error details
     */
    LoginResponseDTO formatErrorResponse(String error);
}
