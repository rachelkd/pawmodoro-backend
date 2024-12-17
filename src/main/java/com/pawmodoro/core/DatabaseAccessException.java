package com.pawmodoro.core;

/**
 * Exception thrown when there is an error accessing the database.
 */
public class DatabaseAccessException extends Exception {
    public DatabaseAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseAccessException(String message) {
        super(message);
    }
}
