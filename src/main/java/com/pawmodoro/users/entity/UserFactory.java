package com.pawmodoro.users.entity;

/**
 * Factory for creating users.
 */
public interface UserFactory {
    /**
     * Creates a new User.
     * @param name the name of the new user
     * @param email the email of the new user
     * @return the new user
     */
    User create(String name, String email);
}
