package com.pawmodoro.users.service.login;

import org.springframework.stereotype.Service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.User;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.login.interface_adapter.LoginResponseDTO;

/**
 * The Login Interactor implements the business logic for user authentication.
 * This class follows the Single Responsibility Principle by handling only login-related logic.
 */
@Service
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    /**
     * Constructs a LoginInteractor with required dependencies.
     * @param userDataAccessObject data access object for user operations
     * @param loginPresenter presenter for formatting responses
     */
    public LoginInteractor(
        LoginUserDataAccessInterface userDataAccessObject,
        LoginOutputBoundary loginPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.loginPresenter = loginPresenter;
    }

    @Override
    public LoginResponseDTO execute(LoginInputData loginInputData)
        throws DatabaseAccessException, UserNotFoundException {
        validateInput(loginInputData);

        // Get user by username to get their email
        User user = userDataAccessObject.get(loginInputData.username());

        try {
            // Authenticate with Supabase using email and password
            User authenticatedUser = userDataAccessObject.authenticate(
                user.getEmail(),
                loginInputData.password());

            // Get the access token from the successful authentication
            String token = userDataAccessObject.getAccessToken();

            return prepareSuccessResponse(authenticatedUser.getName(), token);
        }
        catch (UserNotFoundException exception) {
            throw new InvalidLoginException("Wrong password");
        }
    }

    /**
     * Prepares a success response with the authenticated user's information.
     * @param username the authenticated username
     * @param token the authentication token
     * @return formatted login response
     */
    private LoginResponseDTO prepareSuccessResponse(String username, String token) {
        return loginPresenter.prepareResponse(
            new LoginOutputData(username, token));
    }

    private void validateInput(LoginInputData data) {
        if (data.username() == null || data.username().trim().isEmpty()) {
            throw new InvalidLoginException("Username is required");
        }
        if (data.password() == null || data.password().trim().isEmpty()) {
            throw new InvalidLoginException("Password is required");
        }
    }
}
