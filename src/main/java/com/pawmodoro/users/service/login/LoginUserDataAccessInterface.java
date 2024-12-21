package com.pawmodoro.users.service.login;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.AuthenticatedUser;
import com.pawmodoro.users.entity.User;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * Interface for user authentication data access.
 */
public interface LoginUserDataAccessInterface {
    /**
     * Authenticates a user with their email and password.
     * @param email the user's email
     * @param password the user's password
     * @return AuthenticatedUser containing the user and their tokens
     * @throws DatabaseAccessException if authentication fails
     * @throws UserNotFoundException if the user is not found
     */
    AuthenticatedUser authenticate(String email, String password) throws DatabaseAccessException, UserNotFoundException;

    /**
     * Retrieves a user by their username.
     * @param username the username to look up
     * @return the User object if found
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    User get(String username) throws UserNotFoundException, DatabaseAccessException;

    /**
     * Checks if a user exists by their username.
     * @param username the username to check
     * @return true if the user exists, false otherwise
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    boolean existsByName(String username) throws DatabaseAccessException;
}
