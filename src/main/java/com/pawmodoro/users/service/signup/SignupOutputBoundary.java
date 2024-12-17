package com.pawmodoro.users.service.signup;

import com.pawmodoro.users.service.signup.interface_adapter.SignupResponseDTO;

/**
 * Output boundary for formatting signup responses.
 * This interface defines how the use case output should be formatted into responses.
 * Consistently uses SignupOutputData for both success and error cases to maintain
 * clean separation between use case and presentation layers.
 */
public interface SignupOutputBoundary {
    /**
     * Formats a signup response, whether successful or not.
     * @param outputData the output data from the signup use case
     * @return formatted signup response DTO
     */
    SignupResponseDTO prepareResponse(SignupOutputData outputData);
}
