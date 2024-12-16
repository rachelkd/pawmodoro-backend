package com.pawmodoro.users.login;

/**
 * Exception thrown when a login attempt fails due to invalid credentials.
 * Includes the formatted response to be sent back to the client.
 */
public class InvalidLoginException extends RuntimeException {
    private final LoginResponseDTO response;

    public InvalidLoginException(String message, LoginResponseDTO response) {
        super(message);
        this.response = response;
    }

    public LoginResponseDTO getResponse() {
        return response;
    }
}
