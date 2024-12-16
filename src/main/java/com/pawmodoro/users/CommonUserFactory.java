package com.pawmodoro.users;

/**
 * Factory for creating CommonUser objects.
 */
public class CommonUserFactory implements UserFactory {

    @Override
    public User create(String name, String email) {
        return new CommonUser(name, email);
    }
}
