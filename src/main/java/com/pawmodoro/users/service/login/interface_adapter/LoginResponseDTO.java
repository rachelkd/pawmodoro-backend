package com.pawmodoro.users.service.login.interface_adapter;

/**
 * Data Transfer Object for login responses.
 * Uses records for immutable data.
 * @param success whether the login was successful
 * @param token the token for authentication (null if login failed)
 * @param message success or error message
 * @param username the username of the logged-in user (null if login failed)
 */
public record LoginResponseDto(
    boolean success,
    String token,
    String message,
    String username) {

}
