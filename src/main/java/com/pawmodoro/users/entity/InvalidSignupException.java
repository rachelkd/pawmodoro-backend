package com.pawmodoro.users.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when signup validation fails.
 * This is a runtime exception as it represents a validation error that should be handled
 * at the application level rather than requiring explicit exception handling.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSignupException extends RuntimeException {

    /**
     * Constructs an InvalidSignupException with an error message.
     * @param message the error message
     */
    public InvalidSignupException(String message) {
        super(message);
    }
}
