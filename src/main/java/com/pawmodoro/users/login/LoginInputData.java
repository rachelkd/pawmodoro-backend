package com.pawmodoro.users.login;

/**
 * Input data object for the login use case.
 * This record encapsulates the data required by the login use case.
 * @param username the username or email of the user attempting to login
 * @param password the user's password
 */
public record LoginInputData(
    String username,
    String password) {}
