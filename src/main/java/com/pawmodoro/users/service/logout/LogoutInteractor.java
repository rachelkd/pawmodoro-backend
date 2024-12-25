package com.pawmodoro.users.service.logout;

import org.springframework.stereotype.Service;

import com.pawmodoro.users.entity.InvalidLogoutException;

/**
 * The Logout Interactor implements the business logic for user logout.
 */
@Service
public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutUserDataAccessInterface userDataAccessObject;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
    }

    @Override
    public void execute(String token) {
        final boolean success = userDataAccessObject.logout(token);
        if (!success) {
            throw new InvalidLogoutException("Failed to logout");
        }
    }
}
