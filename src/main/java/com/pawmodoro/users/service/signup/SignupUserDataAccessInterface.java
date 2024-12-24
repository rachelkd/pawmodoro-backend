package com.pawmodoro.users.service.signup;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.AuthenticationToken;
import com.pawmodoro.users.entity.User;

/**
 * Interface for user signup data access.
 * This interface defines the contract for user signup operations.
 */
public interface SignupUserDataAccessInterface {
    /**
     * Checks if a user with the given username exists.
     * @param username the username to check
     * @return true if a user with the username exists, false otherwise
     * @throws DatabaseAccessException if there is a database error
     */
    boolean existsByName(String username) throws DatabaseAccessException;

    /**
     * Checks if a user with the given email exists.
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     * @throws DatabaseAccessException if there is a database error
     */
    boolean existsByEmail(String email) throws DatabaseAccessException;

    /**
     * Saves a new user with the given password and returns authentication tokens.
     * @param user the user to save
     * @param password the user's password
     * @return authentication tokens for the new user
     * @throws DatabaseAccessException if there is a database error
     */
    AuthenticationToken save(User user, String password) throws DatabaseAccessException;
}
