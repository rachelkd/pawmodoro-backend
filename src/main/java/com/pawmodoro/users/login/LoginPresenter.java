package com.pawmodoro.users.login;

import org.springframework.stereotype.Component;

/**
 * REST API implementation of the LoginPresenter.
 * This class formats login responses for the REST API.
 */
@Component
public class LoginPresenter implements LoginOutputBoundary {

    @Override
    public LoginResponseDTO formatSuccessResponse(LoginOutputData outputData) {
        return new LoginResponseDTO(
            true,
            outputData.token(),
            "Login successful",
            outputData.username());
    }

    @Override
    public LoginResponseDTO formatErrorResponse(String error) {
        return new LoginResponseDTO(
            false,
            null,
            error,
            null);
    }
}
