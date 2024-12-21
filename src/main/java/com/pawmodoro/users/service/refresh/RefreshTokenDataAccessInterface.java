package com.pawmodoro.users.service.refresh;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.AuthenticationToken;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * Interface for refresh token data access.
 */
public interface RefreshTokenDataAccessInterface {
    /**
     * Refreshes the authentication tokens using a refresh token.
     * @param refreshToken the refresh token to use
     * @return new AuthenticationToken containing new access and refresh tokens
     * @throws DatabaseAccessException if token refresh fails
     * @throws UserNotFoundException if the user associated with the token is not found
     */
    AuthenticationToken refreshTokens(String refreshToken) throws DatabaseAccessException, UserNotFoundException;
}
