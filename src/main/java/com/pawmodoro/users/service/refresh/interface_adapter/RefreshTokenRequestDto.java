package com.pawmodoro.users.service.refresh.interface_adapter;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for refresh token requests.
 * This record encapsulates the data sent by the client for token refresh.
 * Contains validation annotations to ensure data integrity before processing.
 * @param refreshToken the refresh token to use for getting new tokens
 */
public record RefreshTokenRequestDto(
    @NotBlank(message = "Refresh token is required")
    String refreshToken) {

}
