package com.pawmodoro.users.login;

/**
 * Output data for the login use case.
 * This class represents the data that comes out of the use case interactor,
 * before it gets formatted by the presenter.
 */
public class LoginOutputData {
    private final String username;
    private final String token;
    private final boolean success;
    private final String error;

    /**
     * Creates a successful login output.
     * @param username the username of the logged-in user
     * @param token the authentication token
     */
    public LoginOutputData(String username, String token) {
        this.username = username;
        this.token = token;
        this.success = true;
        this.error = null;
    }

    /**
     * Creates a failed login output.
     * @param error the error message
     */
    public LoginOutputData(String error) {
        this.username = null;
        this.token = null;
        this.success = false;
        this.error = error;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
