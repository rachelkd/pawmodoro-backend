package com.pawmodoro.users.entity;

/**
 * Exception thrown when attempting to register with an email that is already in use.
 * This is separate from other signup validation errors to allow for different HTTP status codes.
 */
public class EmailAlreadyRegisteredException extends InvalidSignupException {
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
