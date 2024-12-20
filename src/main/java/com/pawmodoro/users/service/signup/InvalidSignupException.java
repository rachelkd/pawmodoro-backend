package com.pawmodoro.users.service.signup;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.pawmodoro.users.service.signup.interface_adapter.SignupResponseDto;

/**
 * Exception thrown when signup validation fails.
 * This is a runtime exception as it represents a validation error that should be handled
 * at the application level rather than requiring explicit exception handling.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSignupException extends RuntimeException {
    private final transient SignupResponseDto response;

    /**
     * Constructs an InvalidSignupException with an error message and response DTO.
     * @param message the error message
     */
    public InvalidSignupException(String message) {
        super(message);
        this.response = SignupResponseDto.error(message);
    }

    /**
     * Gets the response DTO containing error details.
     * @return the error response DTO
     */
    public SignupResponseDto getResponse() {
        return response;
    }
}
