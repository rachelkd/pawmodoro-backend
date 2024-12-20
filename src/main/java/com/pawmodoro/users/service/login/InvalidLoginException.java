package com.pawmodoro.users.service.login;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.pawmodoro.users.service.login.interface_adapter.LoginResponseDto;

/**
 * Exception thrown when login validation fails.
 * This is a runtime exception as it represents a validation error that should be handled
 * at the application level rather than requiring explicit exception handling.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidLoginException extends RuntimeException {
    private final transient LoginResponseDto response;

    /**
     * Constructs an InvalidLoginException with an error message and response DTO.
     * @param message the error message
     */
    public InvalidLoginException(String message) {
        super(message);
        this.response = new LoginResponseDto(false, null, message, null);
    }

    /**
     * Gets the response DTO containing error details.
     * @return the error response DTO
     */
    public LoginResponseDto getResponse() {
        return response;
    }
}
