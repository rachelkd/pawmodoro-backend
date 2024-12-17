package com.pawmodoro.users.service.signup.interface_adapter;

/**
 * Data Transfer Object for signup responses.
 * Uses records for immutable data.
 * @param username the username of the newly created account
 * @param message success or error message
 * @param success whether the signup was successful
 */
public record SignupResponseDTO(
    String username,
    String message,
    boolean success) {
    /**
     * Creates a success response.
     * @param username the username of the newly created account
     * @return a success response DTO
     */
    public static SignupResponseDTO success(String username) {
        return new SignupResponseDTO(username, "User successfully created", true);
    }

    /**
     * Creates an error response.
     * @param message the error message
     * @return an error response DTO
     */
    public static SignupResponseDTO error(String message) {
        return new SignupResponseDTO(null, message, false);
    }
}
