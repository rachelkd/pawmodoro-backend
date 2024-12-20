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
    public LoginResponseDto prepareResponse(LoginOutputData outputData) {
        final LoginResponseDto response;
        if (outputData.isSuccess()) {
            response = new LoginResponseDto(true, outputData.getToken(), "Login successful", outputData.getUsername());
        }
        else {
            response = new LoginResponseDto(false, null, outputData.getError(), null);
        }
        return response;
    }
}
