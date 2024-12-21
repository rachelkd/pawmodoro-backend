package com.pawmodoro.users.service.refresh;

/**
 * Input data for the refresh token use case.
 * Uses records for immutable data.
 * @param refreshToken the refresh token to use for getting a new access token
 */
public record RefreshTokenInputData(
    String refreshToken) {

}
