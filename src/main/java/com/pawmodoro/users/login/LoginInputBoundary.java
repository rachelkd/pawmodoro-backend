package com.pawmodoro.users.login;

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
     * @throws InvalidLoginException if login credentials are invalid, containing the formatted error response
     */
    LoginResponseDTO execute(LoginInputData loginInputData) throws InvalidLoginException;
}
