package com.pawmodoro.users.service.signup;

import org.springframework.stereotype.Service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.AuthenticationToken;
import com.pawmodoro.users.entity.User;
import com.pawmodoro.users.entity.UserFactory;
import com.pawmodoro.users.service.signup.interface_adapter.SignupResponseDto;

/**
 * The Signup Interactor implements the business logic for user registration.
 * This class follows the Single Responsibility Principle by handling only signup-related logic.
 */
@Service
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary signupPresenter;
    private final UserFactory userFactory;

    /**
     * Constructs a SignupInteractor with required dependencies.
     * @param userDataAccessObject data access object for user operations
     * @param signupPresenter presenter for formatting responses
     * @param userFactory factory for creating User entities
     */
    public SignupInteractor(
        SignupUserDataAccessInterface userDataAccessObject,
        SignupOutputBoundary signupPresenter,
        UserFactory userFactory) {
        this.userDataAccessObject = userDataAccessObject;
        this.signupPresenter = signupPresenter;
        this.userFactory = userFactory;
    }

    @Override
    public SignupResponseDto execute(SignupInputData signupInputData) throws DatabaseAccessException {
        if (userDataAccessObject.existsByName(signupInputData.getUsername())) {
            throw new InvalidSignupException("Username is already taken");
        }

        if (userDataAccessObject.existsByEmail(signupInputData.getEmail())) {
            throw new EmailAlreadyRegisteredException("Email is already registered");
        }

        final User user = userFactory.create(
            signupInputData.getUsername(),
            signupInputData.getEmail());

        // Save the user and get authentication tokens
        final AuthenticationToken tokens = userDataAccessObject.save(user, signupInputData.getPassword());

        return signupPresenter.prepareResponse(
            new SignupOutputData(user.getName(), signupInputData.getEmail(), tokens));
    }
}
