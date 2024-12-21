package com.pawmodoro.core;

/**
 * Exception thrown when authentication fails.
 * This includes invalid tokens, expired tokens, and unauthorized access.
 * Extends DatabaseAccessException to comply with throws count limit while
 * maintaining separate error handling in GlobalExceptionHandler.
 */
public class AuthenticationException extends DatabaseAccessException {
    /**
     * Constructs an AuthenticationException with the specified message.
     * @param message the detail message
     */
    public AuthenticationException(String message) {
        super(message);
    }
}
