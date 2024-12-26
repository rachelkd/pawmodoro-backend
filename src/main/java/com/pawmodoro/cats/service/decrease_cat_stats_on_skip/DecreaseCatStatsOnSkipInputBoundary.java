package com.pawmodoro.cats.service.decrease_cat_stats_on_skip;

import com.pawmodoro.cats.service.decrease_cat_stats_on_skip.interface_adapter.DecreaseCatStatsOnSkipResponseDto;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Input boundary for the Decrease Cat Stats On Skip use case.
 * Defines the contract for executing the use case.
 */
public interface DecreaseCatStatsOnSkipInputBoundary {
    /**
     * Executes the decrease cat stats on skip use case.
     * @param inputData the input data containing the owner's username
     * @return the response DTO containing the updated cat information
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    DecreaseCatStatsOnSkipResponseDto execute(DecreaseCatStatsOnSkipInputData inputData) throws DatabaseAccessException;
}
