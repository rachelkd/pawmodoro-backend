package com.pawmodoro.users.entity;

/**
 * Entity representing an authenticated user with their tokens.
 */
public record AuthenticatedUser(User user, AuthenticationToken tokens) {}
