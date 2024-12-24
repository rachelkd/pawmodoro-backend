package com.pawmodoro.cats.service.delete_cat;

import com.pawmodoro.cats.service.delete_cat.interface_adapter.DeleteCatResponseDto;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Input boundary for the Delete Cat use case.
 * Defines the contract for executing the delete cat operation.
 */
public interface DeleteCatInputBoundary {
    /**
     * Executes the delete cat operation.
     * @param inputData the input data containing the cat name and owner username
     * @return the response DTO
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    DeleteCatResponseDto execute(DeleteCatInputData inputData) throws DatabaseAccessException;
}
