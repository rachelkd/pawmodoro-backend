package com.pawmodoro.cats.service.get_all_cats;

import com.pawmodoro.cats.entity.CatAuthenticationException;
import com.pawmodoro.cats.service.get_all_cats.interface_adapter.GetAllCatsResponseDto;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Input boundary for the Get All Cats use case.
 * Defines the contract for executing the use case.
 */
public interface GetAllCatsInputBoundary {
    /**
     * Executes the Get All Cats use case.
     * @param getAllCatsInputData input data containing the owner's username
     * @return GetAllCatsResponseDTO containing the formatted response
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws CatAuthenticationException if the authentication fails
     */
    GetAllCatsResponseDto execute(
        GetAllCatsInputData getAllCatsInputData) throws DatabaseAccessException, CatAuthenticationException;
}
