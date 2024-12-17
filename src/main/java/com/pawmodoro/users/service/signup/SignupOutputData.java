package com.pawmodoro.users.service.signup;

/**
 * Output data for the signup use case.
 * This class represents the data that comes out of the use case interactor,
 * before it gets formatted by the presenter.
 */
public class SignupOutputData {
    private final String username;
    private final String email;
    private final boolean success;
    private final String error;

    /**
     * Creates a successful signup output.
     * @param username the username of the created account
     * @param email the email of the created account
     */
    public SignupOutputData(String username, String email) {
        this.username = username;
        this.email = email;
        this.success = true;
        this.error = null;
    }

    /**
     * Creates a failed signup output.
     * @param error the error message
     */
    public SignupOutputData(String error) {
        this.username = null;
        this.email = null;
        this.success = false;
        this.error = error;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
