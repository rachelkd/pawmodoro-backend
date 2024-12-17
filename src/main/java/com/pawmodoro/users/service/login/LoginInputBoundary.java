package com.pawmodoro.users.service.login;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.login.interface_adapter.LoginResponseDTO;

/**
 * Input boundary for the login use case.
 * This interface defines the contract for executing the login operation
 * and follows the Interface Segregation Principle.
 */
public interface LoginInputBoundary {
    /**
     * Executes the login use case with the provided input data.
     * The interactor implementing this interface will handle both the business logic
     * and calling the presenter to format the response.
     * @param loginInputData the input data containing login credentials
     * @return LoginResponseDTO the formatted response ready to be sent to the client
     * @throws InvalidLoginException if login validation fails
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there is a database error
     */
    LoginResponseDTO execute(LoginInputData loginInputData)
        throws UserNotFoundException, DatabaseAccessException;
}
