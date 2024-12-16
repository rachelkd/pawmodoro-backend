package com.pawmodoro.users.login;

/**
 * Data Transfer Object for login responses.
 * This record encapsulates the data returned to the client after a login attempt.
 * @param success whether the login attempt was successful
 * @param token token for successful login (null if login failed)
 * @param message success or error message
 * @param username the username of the logged-in user (null if login failed)
 */
public record LoginResponseDTO(
    boolean success,
    String token,
    String message,
    String username) {}
