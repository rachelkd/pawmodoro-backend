package com.pawmodoro.users.service.login.interface_adapter;

import com.pawmodoro.users.entity.AuthenticationToken;

/**
 * Data Transfer Object for successful login responses.
 * Error cases are handled by GlobalExceptionHandler.
 * @param accessToken the JWT access token
 * @param refreshToken the JWT refresh token
 * @param username the username of the logged-in user
 */
public record LoginResponseDto(
    String accessToken,
    String refreshToken,
    String username) {

    /**
     * Creates a login response from authentication tokens and username.
     */
    public static LoginResponseDto from(String username, AuthenticationToken tokens) {
        return new LoginResponseDto(
            tokens.accessToken(),
            tokens.refreshToken(),
            username);
    }
}
