package com.pawmodoro.users.service.refresh;

import com.pawmodoro.users.entity.AuthenticationToken;

/**
 * Output data for the refresh token use case.
 */
public class RefreshTokenOutputData {
    private final AuthenticationToken tokens;

    /**
     * Creates a successful refresh token output.
     * @param tokens the new authentication tokens
     */
    public RefreshTokenOutputData(AuthenticationToken tokens) {
        this.tokens = tokens;
    }

    public AuthenticationToken getTokens() {
        return tokens;
    }
}
