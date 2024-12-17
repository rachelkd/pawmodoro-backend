package com.pawmodoro.users.entity;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
