package com.pawmodoro.cats.service.create_cat;

import com.pawmodoro.cats.service.create_cat.interface_adapter.CreateCatResponseDto;

/**
 * Output boundary for the Create Cat use case.
 * Defines the contract for presenting the results of creating a cat.
 */
public interface CreateCatOutputBoundary {
    /**
     * Prepares the response for the create cat operation.
     * @param outputData the output data containing created cat
     * @return the response data
     */
    CreateCatResponseDto prepareResponse(CreateCatOutputData outputData);
}
