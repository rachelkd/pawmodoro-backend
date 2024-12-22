package com.pawmodoro.users.service.login.interface_adapter;

import com.pawmodoro.users.entity.AuthenticationToken;

/**
 * Data Transfer Object for successful login responses.
 * Error cases are handled by GlobalExceptionHandler.
 * @param username the username of the logged-in user
 * @param accessToken the JWT access token
 * @param refreshToken the JWT refresh token
 * @param expiresIn number of seconds until the access token expires
 * @param expiresAt timestamp when the access token expires
 */
public record LoginResponseDto(
    String username,
    String accessToken,
    String refreshToken,
    int expiresIn,
    long expiresAt) {

    /**
     * Creates a login response from authentication tokens and username.
     */
    public static LoginResponseDto from(String username, AuthenticationToken tokens) {
        return new LoginResponseDto(
            username,
            tokens.accessToken(),
            tokens.refreshToken(),
            tokens.expiresIn(),
            tokens.expiresAt());
    }
}
