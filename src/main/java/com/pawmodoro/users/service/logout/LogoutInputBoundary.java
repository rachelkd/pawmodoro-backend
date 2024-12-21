package com.pawmodoro.users.service.logout;

/**
 * Input boundary for the logout use case.
 */
public interface LogoutInputBoundary {
    /**
     * Executes the logout use case.
     * @param token the access token to be used for logout
     * @throws InvalidLogoutException if logout fails
     */
    void execute(String token);
}