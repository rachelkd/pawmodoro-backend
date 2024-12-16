package com.pawmodoro.users;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
