package com.pawmodoro.users.login;

import com.pawmodoro.users.User;
import com.pawmodoro.users.UserNotFoundException;
import entity.exceptions.DatabaseAccessException;

/**
 * Interface for user authentication data access.
 * This interface defines the contract for user authentication operations.
 */
public interface LoginUserDataAccessInterface {
    /**
     * Checks if a user exists by their username.
     * @param username the username to check
     * @return true if the user exists, false otherwise
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    boolean existsByName(String username) throws DatabaseAccessException;

    /**
     * Retrieves a user by their username.
     * @param username the username to look up
     * @return the User object if found
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    User get(String username) throws UserNotFoundException, DatabaseAccessException;

    /**
     * Authenticates a user with their email and password.
     * @param email the user's email
     * @param password the user's password
     * @return the authenticated User object
     * @throws DatabaseAccessException if authentication fails or there is a database error
     * @throws UserNotFoundException if the user is not found
     */
    User authenticate(String email, String password) throws DatabaseAccessException, UserNotFoundException;

    /**
     * Gets the access token from the most recent successful authentication.
     * @return the Supabase access token
     */
    String getAccessToken();
}
