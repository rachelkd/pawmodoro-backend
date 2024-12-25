package com.pawmodoro.cats.service.update_cat_happiness;

import com.pawmodoro.cats.service.update_cat_happiness.interface_adapter.UpdateCatHappinessResponseDto;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Input boundary for the Update Cat Happiness use case.
 * Defines the contract for executing the update cat happiness operation.
 */
public interface UpdateCatHappinessInputBoundary {
    /**
     * Executes the update cat happiness operation with the given input data.
     * @param inputData the input data containing cat name, owner username, and change amount
     * @return the output data containing the updated cat
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws AuthenticationException if the authentication token is invalid or missing
     */
    UpdateCatHappinessResponseDto execute(
        UpdateCatHappinessInputData inputData) throws DatabaseAccessException;
}
