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
        return LoginResponseDto.from(
            outputData.getUsername(),
            outputData.getTokens());
    }
}
