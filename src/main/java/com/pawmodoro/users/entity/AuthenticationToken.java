package com.pawmodoro.users.entity;

/**
 * Value object representing authentication tokens.
 * @param accessToken JWT access token
 * @param refreshToken JWT refresh token
 * @param expiresIn number of seconds until the access token expires
 * @param expiresAt timestamp when the access token expires
 */
public record AuthenticationToken(
    String accessToken,
    String refreshToken,
    int expiresIn,
    long expiresAt) {

}
