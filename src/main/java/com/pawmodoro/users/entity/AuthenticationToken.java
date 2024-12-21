package com.pawmodoro.users.entity;

/**
 * Value object representing authentication tokens.
 * @param accessToken JWT access token
 * @param refreshToken JWT refresh token
 */
public record AuthenticationToken(String accessToken, String refreshToken) {}
