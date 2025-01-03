package com.pawmodoro.users.service.signup;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.InvalidSignupException;
import com.pawmodoro.users.service.signup.interface_adapter.SignupResponseDto;

/**
 * Input boundary for the signup use case.
 * This interface defines the contract for executing the signup operation
 * and follows the Interface Segregation Principle.
 */
public interface SignupInputBoundary {
    /**
     * Executes the signup use case with the provided input data.
     * The interactor implementing this interface will handle both the business logic
     * and calling the presenter to format the response.
     * @param signupInputData the input data containing signup information
     * @return SignupResponseDTO the formatted response ready to be sent to the client
     * @throws InvalidSignupException if signup validation fails
     * @throws DatabaseAccessException if there is a database error
     */
    SignupResponseDto execute(SignupInputData signupInputData) throws DatabaseAccessException;
}
