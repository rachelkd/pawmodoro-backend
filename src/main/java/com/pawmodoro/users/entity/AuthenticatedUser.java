package com.pawmodoro.users.entity;

/**
 * Entity representing an authenticated user with their tokens.
 * @param user the user
 * @param tokens the authentication tokens
 */
public record AuthenticatedUser(User user, AuthenticationToken tokens) {

}
