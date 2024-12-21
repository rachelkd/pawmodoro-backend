package com.pawmodoro.users.service.refresh.interface_adapter;

import com.pawmodoro.users.entity.AuthenticationToken;

/**
 * Data Transfer Object for successful token refresh responses.
 * Error cases are handled by GlobalExceptionHandler.
 * @param accessToken the new JWT access token
 * @param refreshToken the new JWT refresh token
 */
public record RefreshTokenResponseDto(
    String accessToken,
    String refreshToken) {
    /**
     * Creates a refresh token response from authentication tokens.
     */
    public static RefreshTokenResponseDto from(AuthenticationToken tokens) {
        return new RefreshTokenResponseDto(
            tokens.accessToken(),
            tokens.refreshToken());
    }
}
