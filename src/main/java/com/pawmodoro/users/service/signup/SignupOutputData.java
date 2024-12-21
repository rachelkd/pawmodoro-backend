package com.pawmodoro.users.service.signup;

import com.pawmodoro.users.entity.AuthenticationToken;

/**
 * Output data for the signup use case.
 * This class represents the data that comes out of the use case interactor,
 * before it gets formatted by the presenter.
 */
public class SignupOutputData {
    private final String username;
    private final String email;
    private final AuthenticationToken tokens;

    /**
     * Creates a signup output.
     * @param username the username of the created account
     * @param email the email of the created account
     * @param tokens the authentication tokens for the new user
     */
    public SignupOutputData(String username, String email, AuthenticationToken tokens) {
        this.username = username;
        this.email = email;
        this.tokens = tokens;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public AuthenticationToken getTokens() {
        return tokens;
    }
}
