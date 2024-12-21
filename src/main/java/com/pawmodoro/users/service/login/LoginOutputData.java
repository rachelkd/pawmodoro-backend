package com.pawmodoro.users.service.login;

import com.pawmodoro.users.entity.AuthenticationToken;

/**
 * Output data for the login use case.
 */
public class LoginOutputData {
    private final String username;
    private final AuthenticationToken tokens;

    /**
     * Creates a successful login output.
     * @param username the username of the logged-in user
     * @param tokens the authentication tokens
     */
    public LoginOutputData(String username, AuthenticationToken tokens) {
        this.username = username;
        this.tokens = tokens;
    }

    public String getUsername() {
        return username;
    }

    public AuthenticationToken getTokens() {
        return tokens;
    }
}
