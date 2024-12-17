package com.pawmodoro.users.entity;

import org.springframework.stereotype.Component;

/**
 * Factory for creating CommonUser objects.
 */
@Component
public class CommonUserFactory implements UserFactory {

    @Override
    public User create(String name, String email) {
        return new CommonUser(name, email);
    }
}
