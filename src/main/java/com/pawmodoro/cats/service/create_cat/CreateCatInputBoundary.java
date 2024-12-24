package com.pawmodoro.cats.service.create_cat;

import com.pawmodoro.cats.service.create_cat.interface_adapter.CreateCatResponseDto;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Input boundary for the Create Cat use case.
 * Defines the contract for executing the create cat operation.
 */
public interface CreateCatInputBoundary {
    /**
     * Executes the create cat operation with the given input data.
     * @param inputData the input data containing owner username and cat details
     * @return the output data containing the created cat
     * @throws CatAuthenticationException if there is an authentication error
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    CreateCatResponseDto execute(
        CreateCatInputData inputData) throws DatabaseAccessException;
}
