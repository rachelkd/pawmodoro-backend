package com.pawmodoro.users.service.refresh.interface_adapter;

import com.pawmodoro.users.entity.AuthenticationToken;

/**
 * Data Transfer Object for successful token refresh responses.
 * Error cases are handled by GlobalExceptionHandler.
 * @param accessToken the new JWT access token
 * @param refreshToken the new JWT refresh token
 * @param expiresIn number of seconds until the access token expires
 * @param expiresAt timestamp when the access token expires
 */
public record RefreshTokenResponseDto(
    String accessToken,
    String refreshToken,
    int expiresIn,
    long expiresAt) {
    /**
     * Creates a refresh token response from authentication tokens.
     */
    public static RefreshTokenResponseDto from(AuthenticationToken tokens) {
        return new RefreshTokenResponseDto(
            tokens.accessToken(),
            tokens.refreshToken(),
            tokens.expiresIn(),
            tokens.expiresAt());
    }
}
