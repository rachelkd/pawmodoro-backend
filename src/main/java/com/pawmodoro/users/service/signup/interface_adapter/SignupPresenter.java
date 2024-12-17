package com.pawmodoro.users.service.signup.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.users.service.signup.SignupOutputBoundary;
import com.pawmodoro.users.service.signup.SignupOutputData;

/**
 * Presenter for formatting signup responses.
 * This class implements the output boundary and handles the formatting of responses
 * following the Interface Segregation Principle.
 */
@Component
public class SignupPresenter implements SignupOutputBoundary {

    @Override
    public SignupResponseDTO prepareResponse(SignupOutputData outputData) {
        if (outputData.isSuccess()) {
            return new SignupResponseDTO(
                outputData.getUsername(),
                "User successfully created with email " + outputData.getEmail(),
                true);
        }
        else {
            return new SignupResponseDTO(
                null,
                outputData.getError(),
                false);
        }
    }
}
