package com.pawmodoro.users.login;

import org.springframework.stereotype.Component;

/**
 * REST API implementation of the LoginPresenter.
 * This class formats login responses for the REST API.
 */
@Component
public class LoginPresenter implements LoginOutputBoundary {

    @Override
    public LoginResponseDTO prepareResponse(LoginOutputData outputData) {
        if (outputData.isSuccess()) {
            return new LoginResponseDTO(
                true,
                outputData.getToken(),
                "Login successful",
                outputData.getUsername());
        }
        else {
            return new LoginResponseDTO(
                false,
                null,
                outputData.getError(),
                null);
        }
    }
}
