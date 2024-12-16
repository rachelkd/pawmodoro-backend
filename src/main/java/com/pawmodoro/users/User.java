package com.pawmodoro.users;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getName();

    /**
     * Returns the email of the user.
     * @return the email of the user.
     */
    String getEmail();
}
