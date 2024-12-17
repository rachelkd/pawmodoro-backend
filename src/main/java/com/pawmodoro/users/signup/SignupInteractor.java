package com.pawmodoro.users.signup;

import com.pawmodoro.users.User;
import com.pawmodoro.users.UserFactory;
import org.springframework.stereotype.Service;
import entity.exceptions.DatabaseAccessException;

/**
 * The Signup Interactor implements the business logic for user registration.
 * This class follows the Single Responsibility Principle by handling only signup-related logic.
 */
@Service
public class SignupInteractor implements SignupInputBoundary {
    private static final int MIN_PASSWORD_LENGTH = 6;
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
    public SignupResponseDTO execute(SignupInputData signupInputData) throws DatabaseAccessException {
        validateInput(signupInputData);

        if (userDataAccessObject.existsByName(signupInputData.getUsername())) {
            throw new InvalidSignupException("Username is already taken");
        }

        User user = userFactory.create(
            signupInputData.getUsername(),
            signupInputData.getEmail());

        userDataAccessObject.save(user, signupInputData.getPassword());

        return signupPresenter.prepareResponse(
            new SignupOutputData(user.getName(), signupInputData.getEmail()));
    }

    private void validateInput(SignupInputData data) {
        if (data.getUsername() == null || data.getUsername().trim().isEmpty()) {
            throw new InvalidSignupException("Username is required");
        }
        if (data.getEmail() == null || data.getEmail().trim().isEmpty()) {
            throw new InvalidSignupException("Email is required");
        }
        if (data.getPassword() == null || data.getPassword().trim().isEmpty()) {
            throw new InvalidSignupException("Password is required");
        }
        if (data.getRepeatPassword() == null || data.getRepeatPassword().trim().isEmpty()) {
            throw new InvalidSignupException("Password confirmation is required");
        }
        if (!data.getPassword().equals(data.getRepeatPassword())) {
            throw new InvalidSignupException("Passwords do not match");
        }
        if (data.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidSignupException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        if (!isValidEmail(data.getEmail())) {
            throw new InvalidSignupException("Please enter a valid email address");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
