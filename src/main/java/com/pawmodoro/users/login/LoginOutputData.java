package com.pawmodoro.users.login;

/**
 * Output data object for the login use case.
 * This record encapsulates the result data from the login use case.
 * @param username the username of the logged-in user
 * @param token JWT token for the authenticated session
 */
public record LoginOutputData(
    String username,
    String token) {}
