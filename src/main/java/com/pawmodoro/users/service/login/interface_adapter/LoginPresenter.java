package com.pawmodoro.users.service.login.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.users.service.login.LoginOutputBoundary;
import com.pawmodoro.users.service.login.LoginOutputData;

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
