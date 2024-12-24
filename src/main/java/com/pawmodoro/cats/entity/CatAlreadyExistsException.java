package com.pawmodoro.cats.entity;

/**
 * Exception thrown when a cat already exists for a user.
 */
public class CatAlreadyExistsException extends Exception {
    public CatAlreadyExistsException(String message) {
        super(message);
    }
}
