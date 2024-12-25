package com.pawmodoro.cats.service.update_cat_hunger;

import com.pawmodoro.cats.service.update_cat_hunger.interface_adapter.UpdateCatHungerResponseDto;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Input boundary for the Update Cat Hunger use case.
 * Defines the contract for executing the update cat hunger operation.
 */
public interface UpdateCatHungerInputBoundary {
    /**
     * Executes the update cat hunger operation with the given input data.
     * @param inputData the input data containing cat name, owner username, and change amount
     * @return the output data containing the updated cat
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws AuthenticationException if the authentication token is invalid or missing
     */
    UpdateCatHungerResponseDto execute(
        UpdateCatHungerInputData inputData) throws DatabaseAccessException;
}
