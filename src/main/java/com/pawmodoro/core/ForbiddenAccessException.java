package com.pawmodoro.core;

/**
 * Exception thrown when a user attempts to access or modify a resource they don't have permission for.
 * This is different from AuthenticationException in that the user is authenticated (has a valid token)
 * but lacks the authorization to perform the specific action.
 */
public class ForbiddenAccessException extends DatabaseAccessException {
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
