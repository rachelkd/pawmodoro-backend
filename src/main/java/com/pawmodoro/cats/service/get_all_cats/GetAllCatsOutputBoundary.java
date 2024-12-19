package com.pawmodoro.cats.service.get_all_cats;

import com.pawmodoro.cats.service.get_all_cats.interface_adapter.GetAllCatsResponseDTO;

/**
 * Output boundary for formatting get all cats responses.
 * This interface defines how the use case output should be formatted into responses.
 * Uses separate methods for success and failure cases to make the intent clearer.
 */
public interface GetAllCatsOutputBoundary {
    /**
     * Formats a successful get all cats response.
     * @param outputData the output data from the get all cats use case
     * @return formatted response DTO for success case
     */
    GetAllCatsResponseDTO prepareResponse(GetAllCatsOutputData outputData);
}
