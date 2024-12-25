package com.pawmodoro.users.service.login;

import org.springframework.stereotype.Service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.AuthenticatedUser;
import com.pawmodoro.users.entity.InvalidLoginException;
import com.pawmodoro.users.entity.User;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.login.interface_adapter.LoginResponseDto;

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
    public LoginResponseDto execute(
        LoginInputData loginInputData) throws UserNotFoundException, DatabaseAccessException {
        // Get user by username to get their email
        final User user = userDataAccessObject.get(loginInputData.username());

        try {
            // Authenticate with Supabase using email and password
            final AuthenticatedUser authenticatedUser = userDataAccessObject.authenticate(
                user.getEmail(),
                loginInputData.password());

            return loginPresenter.prepareResponse(
                new LoginOutputData(authenticatedUser.user().getName(), authenticatedUser.tokens()));
        }
        catch (UserNotFoundException exception) {
            throw new InvalidLoginException("Wrong password");
        }
    }
}
