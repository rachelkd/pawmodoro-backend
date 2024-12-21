package com.pawmodoro.users.service.refresh;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.refresh.interface_adapter.RefreshTokenResponseDto;

/**
 * Input boundary for the refresh token use case.
 * This interface defines the contract for executing the token refresh operation
 * and follows the Interface Segregation Principle.
 */
public interface RefreshTokenInputBoundary {
    /**
     * Executes the refresh token use case with the provided input data.
     * @param refreshTokenInputData the input data containing the refresh token
     * @return RefreshTokenResponseDto the formatted response with new tokens
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there is a database error
     */
    RefreshTokenResponseDto execute(
        RefreshTokenInputData refreshTokenInputData) throws UserNotFoundException, DatabaseAccessException;
}
