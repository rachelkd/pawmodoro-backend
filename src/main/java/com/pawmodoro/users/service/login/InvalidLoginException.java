package com.pawmodoro.users.service.login;

import org.springframework.web.bind.annotation.ResponseStatus;

import com.pawmodoro.users.service.login.interface_adapter.LoginResponseDTO;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when login validation fails.
 * This is a runtime exception as it represents a validation error that should be handled
 * at the application level rather than requiring explicit exception handling.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidLoginException extends RuntimeException {
    private final transient LoginResponseDTO response;

    /**
     * Constructs an InvalidLoginException with an error message and response DTO.
     * @param message the error message
     */
    public InvalidLoginException(String message) {
        super(message);
        this.response = new LoginResponseDTO(false, null, message, null);
    }

    /**
     * Gets the response DTO containing error details.
     * @return the error response DTO
     */
    public LoginResponseDTO getResponse() {
        return response;
    }
}
