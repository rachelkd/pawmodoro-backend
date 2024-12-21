package com.pawmodoro.users.service.logout;

/**
 * Interface for user logout data access.
 * This interface defines the contract for user logout operations.
 */
public interface LogoutUserDataAccessInterface {
    /**
     * Logs out the user with the given access token.
     * @param accessToken the user's access token
     * @return true if logout was successful, false otherwise
     */

    boolean logout(String accessToken);
}
