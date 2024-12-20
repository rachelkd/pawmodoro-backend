package com.pawmodoro.users.service.signup.interface_adapter;

/**
 * Data Transfer Object for signup responses.
 * Uses records for immutable data.
 * @param username the username of the newly created account
 * @param message success or error message
 * @param success whether the signup was successful
 */
public record SignupResponseDto(
    String username,
    String message,
    boolean success) {
    /**
     * Creates a success response.
     * @param username the username of the newly created account
     * @return a success response DTO
     */
    public static SignupResponseDto success(String username) {
        return new SignupResponseDto(username, "User successfully created", true);
    }

    /**
     * Creates an error response.
     * @param message the error message
     * @return an error response DTO
     */
    public static SignupResponseDto error(String message) {
        return new SignupResponseDto(null, message, false);
    }
}
