package com.pawmodoro.users.service.signup.interface_adapter;

import com.pawmodoro.users.entity.AuthenticationToken;

/**
 * Data Transfer Object for successful signup responses.
 * Error cases are handled by GlobalExceptionHandler.
 * @param username the username of the newly created account
 * @param accessToken the JWT access token
 * @param refreshToken the JWT refresh token
 * @param expiresIn number of seconds until the access token expires
 * @param expiresAt timestamp when the access token expires
 */
public record SignupResponseDto(
    String username,
    String accessToken,
    String refreshToken,
    int expiresIn,
    long expiresAt) {

    /**
     * Creates a signup response from authentication tokens and username.
     */
    public static SignupResponseDto from(String username, AuthenticationToken tokens) {
        return new SignupResponseDto(
            username,
            tokens.accessToken(),
            tokens.refreshToken(),
            tokens.expiresIn(),
            tokens.expiresAt());
    }
}
