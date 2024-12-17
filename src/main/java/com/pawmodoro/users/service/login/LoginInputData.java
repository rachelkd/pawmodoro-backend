package com.pawmodoro.users.service.login;

/**
 * Input data for the login use case.
 * Uses records for immutable data.
 * @param username the username attempting to log in
 * @param password the password for authentication
 */
public record LoginInputData(
    String username,
    String password) {}
