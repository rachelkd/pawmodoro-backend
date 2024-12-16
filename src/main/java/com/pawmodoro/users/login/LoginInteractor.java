package com.pawmodoro.users.login;

import com.pawmodoro.users.User;
import com.pawmodoro.users.UserNotFoundException;
import entity.exceptions.DatabaseAccessException;
import org.springframework.stereotype.Service;

/**
 * The Login Interactor that implements the login use case.
 * This class contains the business logic for user authentication.
 */
@Service
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    /**
     * Constructs a LoginInteractor with required dependencies.
     * @param userDataAccessInterface data access interface for user operations
     * @param loginOutputBoundary presenter for formatting responses
     */
    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
        LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public LoginResponseDTO execute(LoginInputData loginInputData) throws InvalidLoginException {
        try {
            // First check if the user exists
            if (!userDataAccessObject.existsByName(loginInputData.username())) {
                LoginResponseDTO errorResponse = loginPresenter.formatErrorResponse(
                    "User does not exist.");
                throw new InvalidLoginException("User not found", errorResponse);
            }

            return authenticateUser(loginInputData.username(), loginInputData.password());
        }
        catch (DatabaseAccessException exception) {
            LoginResponseDTO errorResponse = loginPresenter.formatErrorResponse(
                exception.getMessage());
            throw new InvalidLoginException("Authentication failed", errorResponse);
        }
    }

    /**
     * Authenticates a user with the given credentials.
     * @param username the username to authenticate
     * @param password the password to authenticate with
     * @return formatted login response DTO
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws UserNotFoundException if the user is not found
     * @throws InvalidLoginException if the credentials are invalid
     */
    private LoginResponseDTO authenticateUser(String username, String password) throws InvalidLoginException {
        User user;
        // Get the user by username to get their email
        try {
            user = userDataAccessObject.get(username);
        }
        catch (UserNotFoundException exception) {
            LoginResponseDTO errorResponse = loginPresenter.formatErrorResponse(
                exception.getMessage());
            throw new InvalidLoginException("User not found", errorResponse);
        }
        catch (DatabaseAccessException exception) {
            LoginResponseDTO errorResponse = loginPresenter.formatErrorResponse(
                exception.getMessage());
            throw new InvalidLoginException("Database access error", errorResponse);
        }

        try {
            // Attempt to authenticate with Supabase using email and password
            // This will also store the access token in the DAO
            User authenticatedUser = userDataAccessObject.authenticate(user.getEmail(), password);

            // Create output data with user info and the access token from Supabase
            LoginOutputData outputData = new LoginOutputData(
                authenticatedUser.getName(),
                userDataAccessObject.getAccessToken());

            // Format success response using presenter
            return loginPresenter.formatSuccessResponse(outputData);
        }
        catch (UserNotFoundException authException) {
            // Handle authentication failure (wrong password)
            LoginResponseDTO errorResponse = loginPresenter.formatErrorResponse(
                authException.getMessage());
            throw new InvalidLoginException("Invalid credentials", errorResponse);
        }
        catch (DatabaseAccessException exception) {
            LoginResponseDTO errorResponse = loginPresenter.formatErrorResponse(
                "Authentication failed");
            throw new InvalidLoginException("Authentication failed", errorResponse);
        }
    }
}
